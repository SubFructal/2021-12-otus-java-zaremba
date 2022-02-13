package ru.otus.aop.proxy;

public class Main {
    public static void main(String[] args) {
        TestLogging instance = MyDynamicProxy.createInstance();
        instance.calculate(6);
        instance.calculate(10, 15);
        instance.calculate(20, 25, "word");
    }
}
