package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
                    var params = entityClassMetaData.getAllFields().stream().map(field -> {
                        try {
                            return resultSet.getObject(field.getName());
                        } catch (SQLException e) {
                            throw new DataTemplateException(e);
                        }
                    }).toList().toArray();
                    return entityClassMetaData.getConstructor().newInstance(params);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var entities = new ArrayList<T>();
            try {
                while (rs.next()) {
                    var params = entityClassMetaData.getAllFields().stream().map(field -> {
                        try {
                            return rs.getObject(field.getName());
                        } catch (SQLException e) {
                            throw new DataTemplateException(e);
                        }
                    }).toList().toArray();
                    var entity = entityClassMetaData.getConstructor().newInstance(params);
                    entities.add(entity);
                }
                return entities;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            var values = entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
                field.setAccessible(true);
                try {
                    return field.get(object);
                } catch (Exception e) {
                    throw new DataTemplateException(e);
                }
            }).collect(Collectors.toList());
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            var values = entityClassMetaData.getAllFields().stream().map(field -> {
                field.setAccessible(true);
                try {
                    return field.get(object);
                } catch (Exception e) {
                    throw new DataTemplateException(e);
                }
            }).collect(Collectors.toList());
            Collections.rotate(values, values.size() - 1);
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
