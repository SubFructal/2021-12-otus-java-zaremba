package ru.otus.custom_test_framework.app;

public class TestsResult {

    private final int allTestsQuantity;
    private final int passedTestsQuantity;
    private final int failedTestsQuantity;

    public TestsResult(int allTestsQuantity, int passedTestsQuantity, int failedTestsQuantity) {
        this.allTestsQuantity = allTestsQuantity;
        this.passedTestsQuantity = passedTestsQuantity;
        this.failedTestsQuantity = failedTestsQuantity;
    }

    public int getAllTestsQuantity() {
        return allTestsQuantity;
    }

    public int getPassedTestsQuantity() {
        return passedTestsQuantity;
    }

    public int getFailedTestsQuantity() {
        return failedTestsQuantity;
    }
}
