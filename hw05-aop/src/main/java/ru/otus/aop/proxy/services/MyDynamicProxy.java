package ru.otus.aop.proxy.services;

import ru.otus.aop.proxy.annotations.Log;
import ru.otus.aop.proxy.api.TestLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyDynamicProxy {

    private MyDynamicProxy() {

    }

    public static TestLogging createInstance(TestLogging instance) {
        InvocationHandler handler = new MyInvocationHandler(instance);
        return (TestLogging) Proxy.newProxyInstance(TestLogging.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class MyInvocationHandler implements InvocationHandler {
        private final TestLogging testLoggingImplInstance;
        private final List<Method> annotatedByLogAnnotationMethods;
        private final List<Method> nonAnnotatedMethods;

        MyInvocationHandler(TestLogging testLoggingImplInstance) {
            this.testLoggingImplInstance = testLoggingImplInstance;
            annotatedByLogAnnotationMethods = new ArrayList<>();
            nonAnnotatedMethods = new ArrayList<>();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isLogAnnotationPresent(method)) {
                printLog(method, args);
            }
            return method.invoke(testLoggingImplInstance, args);
        }

        private boolean isLogAnnotationPresent(Method interfaceMethod) {
            if (checkMethodPresenceInAnnotatedByLogAnnotationMethodsList(interfaceMethod)) {
                return true;
            }
            if (checkMethodPresenceInNonAnnotatedMethodsList(interfaceMethod)) {
                return false;
            }
            Method currentImplementationMethod = getCurrentImplementationMethod(interfaceMethod);
            return checkLogAnnotationPresence(interfaceMethod, currentImplementationMethod);
        }

        private boolean checkMethodPresenceInAnnotatedByLogAnnotationMethodsList(Method method) {
            return annotatedByLogAnnotationMethods.contains(method);
        }

        private boolean checkMethodPresenceInNonAnnotatedMethodsList(Method method) {
            return nonAnnotatedMethods.contains(method);
        }

        private Method getCurrentImplementationMethod(Method interfaceMethod) {
            try {
                return testLoggingImplInstance.getClass().getMethod(interfaceMethod.getName(),
                        interfaceMethod.getParameterTypes());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        private boolean checkLogAnnotationPresence(Method interfaceMethod, Method currentImplementationMethod) {
            if (currentImplementationMethod.isAnnotationPresent(Log.class)) {
                addMethodToAnnotatedByLogAnnotationMethodsList(interfaceMethod);
                return true;
            } else {
                addMethodToNonAnnotatedMethodsList(interfaceMethod);
                return false;
            }
        }

        private void addMethodToAnnotatedByLogAnnotationMethodsList(Method method) {
            annotatedByLogAnnotationMethods.add(method);
        }

        private void addMethodToNonAnnotatedMethodsList(Method method) {
            nonAnnotatedMethods.add(method);
        }

        private void printLog(Method method, Object... args) {
            System.out.printf("executed method: %s, params: %s%n", method.getName(), Arrays.toString(args));
        }
    }

}
