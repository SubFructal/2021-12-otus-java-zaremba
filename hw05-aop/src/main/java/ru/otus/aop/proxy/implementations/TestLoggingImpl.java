package ru.otus.aop.proxy.implementations;

import ru.otus.aop.proxy.annotations.Log;
import ru.otus.aop.proxy.api.TestLogging;

public class TestLoggingImpl implements TestLogging {

//    @Log
    @Override
    public void calculate(int param) {
        System.out.println("Выполнен метод calculate с одним параметром класса TestLoggingImpl\n");
    }

    @Log
    @Override
    public void calculate(int param1, int param2) {
        System.out.println("Выполнен метод calculate с двумя параметрами класса TestLoggingImpl\n");
    }

//    @Log
    @Override
    public void calculate(int param1, int param2, String param3) {
        System.out.println("Выполнен метод calculate с тремя параметрами класса TestLoggingImpl\n");
    }
}
