package ru.otus.atm.app;

import ru.otus.atm.banknotes.Banknote;
import ru.otus.atm.cells.Cell;

import java.util.ArrayList;
import java.util.List;

public class ATMImpl implements ATM {

    private final List<Cell> cells;

    public ATMImpl(List<Cell> cells) {
        if (cells == null) {
            throw new IllegalArgumentException("Cells can't be null");
        }
        this.cells = cells;
    }

    @Override
    public void putMoney(Banknote banknote) {
        for (Cell cell : cells) {
            if (cell.getNominalId() == banknote.getBanknoteNominal()) {
                cell.addMoney(banknote);
            }
        }
    }

    @Override
    public void showBalance() {
        System.out.printf("Balance is %d\n", getBalance());
    }

    @Override
    public Cash giveCash(int sum) {
        if (sum > getBalance()) {
            throw new IllegalArgumentException("Insufficient funds! Please enter a lower amount");
        }
        if (sum <= 0) {
            throw new IllegalArgumentException("Sum can't be less then 0 or equal 0!");
        }
        if (sum % 100 != 0) {
            throw new IllegalArgumentException("Sum must be multiple 100 Rub");
        }
        var cash = new ArrayList<List<Banknote>>();
        int count;
        int beginSum = sum;
        for (var cell : cells) {
            count = sum / cell.getNominalId();
            if (count == 0) {
                continue;
            }
            List<Banknote> cashFromCurrentCell = cell.takeMoney(count * cell.getNominalId());
            if (cashFromCurrentCell.size() != 0) {
                cash.add(cashFromCurrentCell);
            }
            sum -= cashFromCurrentCell.size() * cell.getNominalId();
        }
        if (sum != 0) {
            throw new BanknotesAbsentException("banknotes absent to give this amount!");
        }
        return new Cash(cash, beginSum);
    }

    private int getBalance() {
        int balance = 0;
        for (var cell : cells) {
            balance += cell.getBalance();
        }
        return balance;
    }

}
