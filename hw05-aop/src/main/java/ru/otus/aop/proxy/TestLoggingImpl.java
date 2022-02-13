package ru.otus.aop.proxy;

public class TestLoggingImpl implements TestLogging {

//    @Log
    @Override
    public void calculate(int param) {
        System.out.println("Выполнен метод calculate с одним параметром\n");
    }

    @Log
    @Override
    public void calculate(int param1, int param2) {
        System.out.println("Выполнен метод calculate с двумя параметрами\n");
    }

//    @Log
    @Override
    public void calculate(int param1, int param2, String param3) {
        System.out.println("Выполнен метод calculate с тремя параметрами\n");
    }
}
