package ru.otus.atm.app;

import ru.otus.atm.banknotes.Banknote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cash {

    private final List<List<Banknote>> cash;
    private final int sum;

    public Cash(List<List<Banknote>> cash, int sum) {
        if (cash == null || sum <= 0) {
            throw new IllegalArgumentException("Cash can't be null, sum can't be less then 0 or equal 0");
        }
        this.cash = cash;
        this.sum = sum;
    }

    @Override
    public String toString() {
        var result = createResult();
        var builder = new StringBuilder();
        for (var entry : result.entrySet()) {
            builder.append(String.format("%dRub banknotes = %d; ", entry.getKey(), entry.getValue()));
        }
        return String.format("Your cash %d Rub: ", sum) + builder;
    }

    private Map<Integer, Integer> createResult() {
        var result = new HashMap<Integer, Integer>();
        for (var list : cash) {
            var banknoteNominal = list.get(0).getBanknoteNominal();
            var banknotesNumber = list.size();
            result.put(banknoteNominal, banknotesNumber);
        }
        return result;
    }

}