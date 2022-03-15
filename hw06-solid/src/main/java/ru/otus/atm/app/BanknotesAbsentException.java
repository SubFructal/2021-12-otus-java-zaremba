package ru.otus.atm.app;

public class BanknotesAbsentException extends RuntimeException {

    public BanknotesAbsentException(String message) {
        super(message);
    }

}