package ru.otus.aop.proxy;

import ru.otus.aop.proxy.api.TestLogging;
import ru.otus.aop.proxy.implementations.TestLoggingImpl;
import ru.otus.aop.proxy.implementations.TestLoggingImpl2;
import ru.otus.aop.proxy.services.MyDynamicProxy;

public class Main {
    public static void main(String[] args) {

        TestLogging originalInstanceTestLoggingImpl = new TestLoggingImpl();
        TestLogging proxyInstanceTestLoggingImpl = MyDynamicProxy.createInstance(originalInstanceTestLoggingImpl);

        TestLogging originalInstanceTestLoggingImpl2 = new TestLoggingImpl2();
        TestLogging proxyInstanceTestLoggingImpl2 = MyDynamicProxy.createInstance(originalInstanceTestLoggingImpl2);

        proxyInstanceTestLoggingImpl.calculate(6);
        proxyInstanceTestLoggingImpl.calculate(12);
        proxyInstanceTestLoggingImpl.calculate(10, 15);
        proxyInstanceTestLoggingImpl.calculate(20, 17);
        proxyInstanceTestLoggingImpl.calculate(20, 25, "word");
        proxyInstanceTestLoggingImpl.calculate(30, 35, "string");

        System.out.println("=======================\n");

        proxyInstanceTestLoggingImpl2.calculate(11);
        proxyInstanceTestLoggingImpl2.calculate(111);
        proxyInstanceTestLoggingImpl2.calculate(222, 333);
        proxyInstanceTestLoggingImpl2.calculate(555, 777);
        proxyInstanceTestLoggingImpl2.calculate(888, 999, "test");
        proxyInstanceTestLoggingImpl2.calculate(000, 606, "second test");

    }
}
