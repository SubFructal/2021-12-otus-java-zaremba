package homework;


import java.util.LinkedList;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    LinkedList<Customer> customerLinkedList = new LinkedList<>();

    public void add(Customer customer) {
        customerLinkedList.push(customer);
    }

    public Customer take() {
//        return null; // это "заглушка, чтобы скомилировать"
        return customerLinkedList.pop();
    }
}
