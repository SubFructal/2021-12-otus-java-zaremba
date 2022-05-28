package ru.otus.dto;

import lombok.Getter;
import lombok.Setter;

public class ClientDto {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private String phoneNumbers;

    public ClientDto() {
    }

    @Override
    public String toString() {
        return "ClientDto{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumbers='" + phoneNumbers + '\'' +
                '}';
    }
}
