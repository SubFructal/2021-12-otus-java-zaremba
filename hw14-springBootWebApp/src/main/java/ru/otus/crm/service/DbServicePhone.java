package ru.otus.crm.service;

import ru.otus.crm.model.Phone;

import java.util.List;
import java.util.Optional;

public interface DbServicePhone {

    Phone savePhone(Phone phone);

    Optional<Phone> getPhone(long id);

    List<Phone> findAll();
}