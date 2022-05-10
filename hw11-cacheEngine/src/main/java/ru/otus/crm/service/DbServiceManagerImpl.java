package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Manager;

import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Manager> cache;

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> dataTemplate,
                                HwCache<String, Manager> cache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = cache;
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = dataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);

                cache.put(Long.toString(managerNo), createdManager);

                return createdManager;
            }
            dataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);

            cache.put(Long.toString(manager.getNo()), manager);

            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {

        var managerOptionalFromCache = Optional.ofNullable(cache.get(Long.toString(no)));
        if (managerOptionalFromCache.isPresent()) {
            return managerOptionalFromCache;
        }

        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = dataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = dataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }
}
