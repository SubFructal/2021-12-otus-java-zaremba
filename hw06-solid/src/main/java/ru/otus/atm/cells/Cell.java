package ru.otus.atm.cells;

import ru.otus.atm.banknotes.Banknote;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Cell {

    private final LinkedList<Banknote> banknotes;

    public Cell() {
        banknotes = new LinkedList<>();
    }

    public abstract int getNominalId();

    public void addMoney(Banknote banknote) {
        banknotes.push(banknote);
    }

    public int getBalance() {
        return getNominalId() * banknotes.size();
    }

    public List<Banknote> takeMoney(int sum) {
        List<Banknote> cash = new ArrayList<>();
        for (int i = 0; i < sum / getNominalId(); i++) {
            if (banknotes.size() == 0) {
                break;
            }
            cash.add(banknotes.pop());
        }
        return cash;
    }

}

