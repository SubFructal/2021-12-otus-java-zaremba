package ru.otus.atm.app;

import ru.otus.atm.banknotes.Banknote;

public interface ATM {

    void putMoney(Banknote banknote);

    void showBalance();

    Cash giveCash(int sum);

}