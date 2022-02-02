package ru.otus.custom_test_framework;

import ru.otus.custom_test_framework.app.TestsRunner;

public class Main {
    public static void main(String[] args) {

        new TestsRunner().run("ru.otus.custom_test_framework.tests.SomeExampleTest");
    }
}
