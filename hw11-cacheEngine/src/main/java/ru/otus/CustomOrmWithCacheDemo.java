package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateJdbc;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DBServiceManager;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * VM options: -Xmx32m -Xms32m -Xlog:gc=debug
 */

public class CustomOrmWithCacheDemo {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger logger = LoggerFactory.getLogger(CustomOrmWithCacheDemo.class);

    public static void main(String[] args) {
//Общая часть
        DataSource dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        TransactionRunner transactionRunner = new TransactionRunnerJdbc(dataSource);
        DbExecutor dbExecutor = new DbExecutorImpl();

//Подготовка к работе с Client
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        DataTemplate<Client> dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient,
                entityClassMetaDataClient);

        HwCache<String, Client> clientCache = new MyCache<>();
        HwListener<String, Client> clientListener = (key, value, action) ->
                logger.info("key:{}, value:{}, action: {}", key, value, action);
        clientCache.addListener(clientListener);

        DBServiceClient dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, clientCache);

//Подготовка к работе с Manager
        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl<>(entityClassMetaDataManager);
        DataTemplate<Manager> dataTemplateManager = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataManager,
                entityClassMetaDataManager);

        HwCache<String, Manager> managerCache = new MyCache<>();
        HwListener<String, Manager> managerListener = (key, value, action) ->
                logger.info("key:{}, value:{}, action: {}", key, value, action);
        managerCache.addListener(managerListener);

        DBServiceManager dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager, managerCache);

        /*
        Проверка функционала кэша
        Работаем на хипе 32 Мб.
        Добавляем в БД и кэш соответственно по 1000 объектов Client и Manager, сохраняем их айдишники в списки.
        Потом по этим айдишникам достаем объекты из БД.
        Там, где в кэше еще есть объекты по этим айдишникам, все достается из кэша.
        А там, где после сборки мусора, которая происходила при недостатке памяти при добавлении следующих объектов,
        в кэше висят null по ключам, нужные объекты достаются непосредственно из БД.
        В логе все это можно пронаблюдать, листенеры в кэшах и логирование в самих методах помогают это сделать.
         */
        List<Long> clientsIds = putToDbAndCache(dbServiceClient);
        List<Long> managersIds = putToDbAndCache(dbServiceManager);

        getFromCacheOrDb(dbServiceClient, clientsIds);
        clientCache.removeListener(clientListener);

        getFromCacheOrDb(dbServiceManager, managersIds);
        managerCache.removeListener(managerListener);

    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }

    private static List<Long> putToDbAndCache(DBServiceClient dbServiceClient) {
        logger.info("put clients to DB and cache...");
        var clientsIds = new ArrayList<Long>();
        for (int i = 0; i < 1000; i++) {
            var savedClient = dbServiceClient.saveClient(new Client("client" + i));
            clientsIds.add(savedClient.getId());
        }
        return clientsIds;
    }

    private static List<Long> putToDbAndCache(DBServiceManager dbServiceManager) {
        logger.info("put managers to DB and cache...");
        var managersIds = new ArrayList<Long>();
        for (int i = 0; i < 1000; i++) {
            var currentManager = new Manager("manager" + i);
            currentManager.setParam1("param" + i);
            var savedManager = dbServiceManager.saveManager(currentManager);
            managersIds.add(savedManager.getNo());
        }
        return managersIds;
    }

    private static void getFromCacheOrDb(DBServiceClient dbServiceClient, List<Long> clientsIds) {
        logger.info("get clients from cache or DB...");
        for (var id : clientsIds) {
            dbServiceClient.getClient(id);
        }
    }

    private static void getFromCacheOrDb(DBServiceManager dbServiceManager, List<Long> managersIds) {
        logger.info("get managers from cache or DB...");
        for (var id : managersIds) {
            dbServiceManager.getManager(id);
        }
    }

}
