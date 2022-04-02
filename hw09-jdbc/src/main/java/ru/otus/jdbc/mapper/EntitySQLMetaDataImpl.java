package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;

    private static final Map<String, String> SELECT_ALL_SQL_OPERATORS = new HashMap<>();
    private static final Map<String, String> SELECT_BY_ID_SQL_OPERATORS = new HashMap<>();
    private static final Map<String, String> INSERT_SQL_OPERATORS = new HashMap<>();
    private static final Map<String, String> UPDATE_SQL_OPERATORS = new HashMap<>();
    private final String inputClassName;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        inputClassName = entityClassMetaData.getName();
    }

    @Override
    public String getSelectAllSql() {
        if (containsInPrepared(SELECT_ALL_SQL_OPERATORS, inputClassName)) {
            return getFromPrepared(SELECT_ALL_SQL_OPERATORS, inputClassName);
        }
        var newSqlSelectAll = String.format("select * from %s", entityClassMetaData.getName().toLowerCase());
        addToPrepared(SELECT_ALL_SQL_OPERATORS, inputClassName, newSqlSelectAll);
        return newSqlSelectAll;
    }

    @Override
    public String getSelectByIdSql() {
        if (containsInPrepared(SELECT_BY_ID_SQL_OPERATORS, inputClassName)) {
            return getFromPrepared(SELECT_BY_ID_SQL_OPERATORS, inputClassName);
        }
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName)
                .collect(Collectors.joining(", "));
        var id = entityClassMetaData.getIdField().getName();
        var tableName = entityClassMetaData.getName().toLowerCase();
        var newSqlSelectById = String.format("select %s, %s from %s where %s = ?", id, fieldsWithoutId, tableName, id);
        addToPrepared(SELECT_BY_ID_SQL_OPERATORS, inputClassName, newSqlSelectById);
        return newSqlSelectById;
    }

    @Override
    public String getInsertSql() {
        if (containsInPrepared(INSERT_SQL_OPERATORS, inputClassName)) {
            return getFromPrepared(INSERT_SQL_OPERATORS, inputClassName);
        }
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName)
                .collect(Collectors.joining(", "));
        var params = entityClassMetaData.getFieldsWithoutId().stream().map(field -> "?")
                .collect(Collectors.joining(", "));
        var tableName = entityClassMetaData.getName().toLowerCase();
        var newSqlInsertOperator = String.format("insert into %s(%s) values (%s)", tableName, fieldsWithoutId, params);
        addToPrepared(INSERT_SQL_OPERATORS, inputClassName, newSqlInsertOperator);
        return newSqlInsertOperator;
    }

    @Override
    public String getUpdateSql() {
        if (containsInPrepared(UPDATE_SQL_OPERATORS, inputClassName)) {
            return getFromPrepared(UPDATE_SQL_OPERATORS, inputClassName);
        }
        var fieldsWithParams = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));
        var tableName = entityClassMetaData.getName().toLowerCase();
        var id = entityClassMetaData.getIdField().getName();
        var newSqlUpdateOperator = String.format("update %s set %s where %s = ?", tableName, fieldsWithParams, id);
        addToPrepared(UPDATE_SQL_OPERATORS, inputClassName, newSqlUpdateOperator);
        return newSqlUpdateOperator;
    }

    private boolean containsInPrepared(Map<String, String> preparedOperators, String inputClassName) {
        return preparedOperators.containsKey(inputClassName);
    }

    private void addToPrepared(Map<String, String> preparedOperators, String inputClassName, String preparedSql) {
        preparedOperators.put(inputClassName, preparedSql);
    }

    private String getFromPrepared(Map<String, String> preparedOperators, String inputClassName) {
        return preparedOperators.get(inputClassName);
    }

}
