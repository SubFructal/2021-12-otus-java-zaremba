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

        Object currentTestObject = null;
        int passedTestCount = 0;
        int failedTestCount = 0;

        for (Method testMethod : testAnnotatedMethods) {
            try {
                currentTestObject = ReflectionHelper.instantiate(inputClassObject);

                callAllMethodsWithSameAnnotation(beforeAnnotatedMethods, currentTestObject);
                callMethod(testMethod, currentTestObject);
                callAllMethodsWithSameAnnotation(afterAnnotatedMethods, currentTestObject);

                passedTestCount++;
            } catch (Exception e) {
                failedTestCount++;
                callAllMethodsWithSameAnnotation(afterAnnotatedMethods, currentTestObject);
            }
        }

        printStatistic((passedTestCount + failedTestCount), passedTestCount, failedTestCount);
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

    private List<Method> searchAllMethodsWithSameAnnotation(Method[] methods, Class<? extends
            Annotation> annotationClass) {
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

    private void callAllMethodsWithSameAnnotation(List<Method> annotatedMethod, Object currentObject) {
        for (Method method : annotatedMethod) {
            callMethod(method, currentObject);
        }
    }

    private Object callMethod(Method method, Object object, Object... args) {
        try {
            return method.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void printStatistic(Object... args) {
        System.out.printf("%nStatistic: all tests count = %d, passed tests count = %d, failed tests count = %d", args);
    }
}