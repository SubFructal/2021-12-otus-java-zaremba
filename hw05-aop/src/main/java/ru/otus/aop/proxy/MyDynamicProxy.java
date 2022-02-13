package ru.otus.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MyDynamicProxy {

    private MyDynamicProxy() {
    }

    public static TestLogging createInstance() {
        InvocationHandler handler = new MyInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(TestLogging.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class MyInvocationHandler implements InvocationHandler {

        private final TestLogging testLoggingImplInstance;

        MyInvocationHandler(TestLogging testLoggingImplInstance) {
            this.testLoggingImplInstance = testLoggingImplInstance;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method methodImpl = getCurrentImplementationMethod(method);
            if (methodImpl.isAnnotationPresent(Log.class)) {
                printLog(method, args);
            }
            return method.invoke(testLoggingImplInstance, args);
        }

        private Method getCurrentImplementationMethod(Method interfaceMethod) {
            try {
                return testLoggingImplInstance.getClass().getMethod(interfaceMethod.getName(),
                        interfaceMethod.getParameterTypes());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        private void printLog(Method method, Object... args) {
            System.out.printf("executed method: %s, params: %s%n", method.getName(), Arrays.toString(args));
        }
    }

}
