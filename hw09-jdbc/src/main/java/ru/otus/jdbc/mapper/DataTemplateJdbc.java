package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), resultSet -> {
            try {
                if (resultSet.next()) {
                    return createObject(entityClassMetaData, resultSet);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(),
                resultSet -> {
                    var objects = new ArrayList<T>();
                    try {
                        while (resultSet.next()) {
                            var newObject = createObject(entityClassMetaData, resultSet);
                            objects.add(newObject);
                        }
                        return objects;
                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            var values = getParamsValues(object);
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            var values = getParamsValues(object);
            var idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            var idFieldValue = idField.get(object);
            values.add(idFieldValue);
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createObject(EntityClassMetaData<T> entityClassMetaData, ResultSet resultSet)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        var newObject = entityClassMetaData.getConstructor().newInstance();
        entityClassMetaData.getAllFields().forEach(field -> {
            try {
                field.setAccessible(true);
                field.set(newObject, resultSet.getObject(field.getName()));
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
        return newObject;
    }

    private List<Object> getParamsValues(T object) {
        return entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
            field.setAccessible(true);
            try {
                return field.get(object);
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).collect(Collectors.toList());
    }
}
