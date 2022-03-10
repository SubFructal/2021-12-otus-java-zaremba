package ru.otus.atm;

import java.util.List;

import ru.otus.atm.app.*;
import ru.otus.atm.banknotes.*;
import ru.otus.atm.cells.*;


public class Demo {

    public static void main(String[] args) {

        var atm = buildATM();

        atm.putMoney(new Banknote100Rub());
        atm.putMoney(new Banknote100Rub());
        atm.putMoney(new Banknote100Rub());
        atm.putMoney(new Banknote200Rub());
        atm.putMoney(new Banknote200Rub());
        atm.putMoney(new Banknote200Rub());
        atm.putMoney(new Banknote500Rub());
        atm.putMoney(new Banknote500Rub());
        atm.putMoney(new Banknote500Rub());
        atm.putMoney(new Banknote1000Rub());
        atm.putMoney(new Banknote1000Rub());
        atm.putMoney(new Banknote2000Rub());
        atm.putMoney(new Banknote5000Rub());

        atm.showBalance();

        var cash = atm.giveCash(5800);
        printData(cash);
        atm.showBalance();

        cash = atm.giveCash(6000);
        printData(cash);
        atm.showBalance();

    }

    private static ATM buildATM() {
        var cell100Rub = new CellFor100RubBanknotes();
        var cell200Rub = new CellFor200RubBanknotes();
        var cell500Rub = new CellFor500RubBanknotes();
        var cell1000Rub = new CellFor1000RubBanknotes();
        var cell2000Rub = new CellFor2000RubBanknotes();
        var cell5000Rub = new CellFor5000RubBanknotes();

        return new ATMImpl(List.of(cell5000Rub, cell2000Rub, cell1000Rub, cell500Rub, cell200Rub, cell100Rub));
    }

    private static void printData(Cash cash) {
        System.out.println(cash);
    }

}