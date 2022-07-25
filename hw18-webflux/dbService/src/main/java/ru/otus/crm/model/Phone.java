package ru.otus.crm.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;


@Table("phone")
public class Phone {

    @Id
    private final Long id;

    @Nonnull
    private final String phoneNumber;

    @Nonnull
    private final Long clientId;

    public Phone(String phoneNumber, Long clientId) {
        this(null, phoneNumber, clientId);
    }

    @PersistenceConstructor
    public Phone(Long id, String phoneNumber, Long clientId) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Long getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}
