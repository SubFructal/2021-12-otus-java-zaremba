package ru.otus.aop.proxy.api;

public interface TestLogging {

    void calculate(int param);

    void calculate(int param1, int param2);

    void calculate(int param1, int param2, String param3);

}
