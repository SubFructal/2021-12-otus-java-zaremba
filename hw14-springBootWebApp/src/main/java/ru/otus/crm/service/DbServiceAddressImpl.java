package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.core.repository.AddressRepository;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbServiceAddressImpl implements DbServiceAddress {
    private static final Logger log = LoggerFactory.getLogger(DbServiceAddressImpl.class);

    private final TransactionManager transactionManager;
    private final AddressRepository addressRepository;

    public DbServiceAddressImpl(TransactionManager transactionManager, AddressRepository addressRepository) {
        this.transactionManager = transactionManager;
        this.addressRepository = addressRepository;
    }

    @Override
    public Address saveAddress(Address address) {
        return transactionManager.doInTransaction(() -> {
            var savedAddress = addressRepository.save(address);
            log.info("updated address: {}", savedAddress);
            return savedAddress;
        });
    }

    @Override
    public Optional<Address> getAddress(long id) {
        var addressOptional = addressRepository.findById(id);
        log.info("address: {}", addressOptional);
        return addressOptional;
    }

    @Override
    public List<Address> findAll() {
        var addressList = new ArrayList<Address>();
        addressRepository.findAll().forEach(addressList::add);
        log.info("addressList:{}", addressList);
        return addressList;
    }
}
