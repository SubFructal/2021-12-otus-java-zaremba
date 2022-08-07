package ru.otus.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.crm.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
