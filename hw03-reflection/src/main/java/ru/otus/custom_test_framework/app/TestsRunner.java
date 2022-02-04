package ru.otus.custom_test_framework.app;

import ru.otus.custom_test_framework.annotations.After;
import ru.otus.custom_test_framework.annotations.Before;
import ru.otus.custom_test_framework.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestsRunner {

    public void run(String className) {
        Class<?> inputClassObject = getClassObjectForInputClass(className);

        Method[] allInputClassMethods = getAllDeclaredMethodsFromInputClass(inputClassObject);

        List<Method> beforeAnnotatedMethods = searchAllMethodsWithSameAnnotation(allInputClassMethods, Before.class);
        List<Method> afterAnnotatedMethods = searchAllMethodsWithSameAnnotation(allInputClassMethods, After.class);
        List<Method> testAnnotatedMethods = searchAllMethodsWithSameAnnotation(allInputClassMethods, Test.class);

        TestsResult testsResult = executeAllTests(inputClassObject, testAnnotatedMethods, beforeAnnotatedMethods,
                afterAnnotatedMethods);

        printTestsResult(testsResult);
    }

    private Class<?> getClassObjectForInputClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Method[] getAllDeclaredMethodsFromInputClass(Class<?> classObject) {
        return classObject.getDeclaredMethods();
    }

    private List<Method> searchAllMethodsWithSameAnnotation(Method[] methods,
                                                            Class<? extends Annotation> annotationClass) {
        List<Method> annotatedMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationClass)) {
                annotatedMethods.add(method);
            }
        }

        if (annotatedMethods.isEmpty()) {
            throw new AnnotatedMethodNotFoundException();
        }
        return annotatedMethods;
    }

    private TestsResult executeAllTests(Class<?> inputClassObject, List<Method> testAnnotatedMethods,
                                        List<Method> beforeAnnotatedMethods, List<Method> afterAnnotatedMethods) {
        Object currentTestObject = null;
        int passedTestCount = 0;
        int failedTestCount = 0;

        for (Method testMethod : testAnnotatedMethods) {
            try {
                currentTestObject = ReflectionHelper.instantiate(inputClassObject);

                callAllMethodsFromInputList(beforeAnnotatedMethods, currentTestObject);
                callMethod(testMethod, currentTestObject);
                callAllMethodsFromInputList(afterAnnotatedMethods, currentTestObject);

                passedTestCount++;
            } catch (Exception e) {
                failedTestCount++;
                callAllMethodsFromInputList(afterAnnotatedMethods, currentTestObject);
            }
        }

        return new TestsResult((passedTestCount + failedTestCount), passedTestCount, failedTestCount);
    }

    private void printTestsResult(TestsResult testsResult) {
        System.out.printf("%nTests result: all tests count = %d, " +
                        "passed tests count = %d, failed tests count = %d", testsResult.getAllTestsQuantity(),
                testsResult.getPassedTestsQuantity(), testsResult.getFailedTestsQuantity());
    }

    private void callAllMethodsFromInputList(List<Method> annotatedMethods, Object currentObject) {
        for (Method method : annotatedMethods) {
            callMethod(method, currentObject);
        }
    }

    private Object callMethod(Method method, Object object, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
