package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", entityClassMetaData.getName().toLowerCase());
    }

    @Override
    public String getSelectByIdSql() {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName)
                .collect(Collectors.joining(", "));
        var id = entityClassMetaData.getIdField().getName();
        var tableName = entityClassMetaData.getName().toLowerCase();

        return String.format("select %s, %s from %s where %s = ?", id, fieldsWithoutId, tableName, id);
    }

    @Override
    public String getInsertSql() {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName)
                .collect(Collectors.joining(", "));
        var params = entityClassMetaData.getFieldsWithoutId().stream().map(field -> "?")
                .collect(Collectors.joining(", "));
        var tableName = entityClassMetaData.getName().toLowerCase();
        return String.format("insert into %s(%s) values (%s)", tableName, fieldsWithoutId, params);
    }

    @Override
    public String getUpdateSql() {
        var fieldsWithParams = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));
        var tableName = entityClassMetaData.getName().toLowerCase();
        var id = entityClassMetaData.getIdField().getName();
        return String.format("update %s set %s where %s = ?", tableName, fieldsWithParams, id);
    }
}
