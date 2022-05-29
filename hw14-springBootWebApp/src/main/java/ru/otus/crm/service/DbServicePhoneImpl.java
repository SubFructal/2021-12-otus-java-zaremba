package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.core.repository.PhoneRepository;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Phone;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbServicePhoneImpl implements DbServicePhone {
    private static final Logger log = LoggerFactory.getLogger(DbServicePhoneImpl.class);

    private final TransactionManager transactionManager;
    private final PhoneRepository phoneRepository;

    public DbServicePhoneImpl(TransactionManager transactionManager, PhoneRepository phoneRepository) {
        this.transactionManager = transactionManager;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Phone savePhone(Phone phone) {
        return transactionManager.doInTransaction(() -> {
            var savedAddress = phoneRepository.save(phone);
            log.info("updated phone: {}", savedAddress);
            return savedAddress;
        });
    }

    @Override
    public Optional<Phone> getPhone(long id) {
        var phoneOptional = phoneRepository.findById(id);
        log.info("phone: {}", phoneOptional);
        return phoneOptional;
    }

    @Override
    public List<Phone> findAll() {
        var phoneList = new ArrayList<Phone>();
        phoneRepository.findAll().forEach(phoneList::add);
        log.info("phoneList:{}", phoneList);
        return phoneList;
    }
}
