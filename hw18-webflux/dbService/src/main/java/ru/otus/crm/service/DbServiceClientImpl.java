package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.core.repository.ClientRepository;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.dto.ClientDto;

import java.util.*;

@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    public DbServiceClientImpl(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client saveClient(ClientDto clientDto) {
        var name = clientDto.getName();
        var street = clientDto.getAddress();
        var phoneNumbers = clientDto.getPhoneNumbers();

        var client = new Client(name, new Address(street, null), getPhonesSet(phoneNumbers));

        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("updated client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        var clientList = new ArrayList<Client>();
        clientRepository.findAll().forEach(clientList::add);
        log.info("clientList:{}", clientList);
        return clientList;
    }

    private Set<Phone> getPhonesSet(String phoneNumbers) {
        String[] phoneNumbersArr = phoneNumbers.split(" ");
        Set<Phone> phonesSet = new HashSet<>();
        for (String phoneNumber : phoneNumbersArr) {
            phonesSet.add(new Phone(phoneNumber, null));
        }
        return phonesSet;
    }
}
