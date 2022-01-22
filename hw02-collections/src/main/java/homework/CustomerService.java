package homework;

import java.util.*;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private TreeMap<Customer, String> customerStringMap = new TreeMap<>(new Comparator<Customer>() {
        @Override
        public int compare(Customer o1, Customer o2) {
            return (int) (o1.getScores() - o2.getScores());
        }
    });

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
//        return null; // это "заглушка, чтобы скомилировать"
        if (customerStringMap.isEmpty()) {
            return null;
        }
        Map.Entry<Customer, String> firstEntry = customerStringMap.firstEntry();
        Customer key = new Customer(firstEntry.getKey().getId(), firstEntry.getKey().getName(),
                firstEntry.getKey().getScores());
        return Map.entry(key, firstEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
//        return null; // это "заглушка, чтобы скомилировать"
        Map.Entry<Customer, String> middleEntry = customerStringMap.higherEntry(customer);
        if (middleEntry == null) {
            return null;
        }
        Customer key = new Customer(middleEntry.getKey().getId(), middleEntry.getKey().getName(),
                middleEntry.getKey().getScores());
        return Map.entry(key, middleEntry.getValue());
    }

    public void add(Customer customer, String data) {
        customerStringMap.put(customer, data);
    }
}
