package ru.otus.custom_test_framework.tests;

import ru.otus.custom_test_framework.annotations.After;
import ru.otus.custom_test_framework.annotations.Before;
import ru.otus.custom_test_framework.annotations.Test;
import ru.otus.custom_test_framework.app.TestFailedException;

public class SomeExampleTest {

    // Подготовительные мероприятия. Метод выполнится перед каждым тестом
    @Before
    public void setUp() {
//        throw new TestFailedException();
        System.out.println("\n@Before started");
        System.out.println("Экземпляр класса SomeExampleTest: " + Integer.toHexString(hashCode()));
        System.out.println("@Before passed");
    }

    // Тест
    @Test
    public void anyTest1() {
        System.out.println("\n@Test: anyTest1 started");
        System.out.println("Экземпляр класса SomeExampleTest: " + Integer.toHexString(hashCode()));
        System.out.println("@Test: anyTest1 passed");
    }

    // Тест
    @Test
    public void anyTest2() {
        System.out.println("\n@Test: anyTest2 started");
        System.out.println("Экземпляр класса SomeExampleTest: " + Integer.toHexString(hashCode()));
        System.out.println("@Test: anyTest2 passed");
    }

    // Test
    @Test
    public void anyTest3() {
        throw new TestFailedException();
//        System.out.println("\n@Test: anyTest3 started");
//        System.out.println("Экземпляр класса SomeExampleTest: " + Integer.toHexString(hashCode()));
//        System.out.println("@Test: anyTest3 passed");
    }

    // Завершающие мероприятия. Метод выполнится после каждого теста
    @After
    public void tearDown() {
        System.out.println("\n@After started");
        System.out.println("Экземпляр класса SomeExampleTest: " + Integer.toHexString(hashCode()));
        System.out.println("@After passed");
    }

}
