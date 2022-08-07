package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.selectAllSql = initSelectAllSql(entityClassMetaData);
        this.selectByIdSql = initSelectByIdSql(entityClassMetaData);
        this.insertSql = initInsertSql(entityClassMetaData);
        this.updateSql = initUpdateSql(entityClassMetaData);
    }

    private String initSelectAllSql(EntityClassMetaData<T> entityClassMetaData) {
        return String.format("select * from %s", entityClassMetaData.getName().toLowerCase());
    }

    private String initSelectByIdSql(EntityClassMetaData<T> entityClassMetaData) {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName)
                .collect(Collectors.joining(", "));
        var id = entityClassMetaData.getIdField().getName();
        var tableName = entityClassMetaData.getName().toLowerCase();
        return String.format("select %s, %s from %s where %s = ?", id, fieldsWithoutId, tableName, id);
    }

    private String initInsertSql(EntityClassMetaData<T> entityClassMetaData) {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName)
                .collect(Collectors.joining(", "));
        var params = entityClassMetaData.getFieldsWithoutId().stream().map(field -> "?")
                .collect(Collectors.joining(", "));
        var tableName = entityClassMetaData.getName().toLowerCase();
        return String.format("insert into %s(%s) values (%s)", tableName, fieldsWithoutId, params);
    }

    private String initUpdateSql(EntityClassMetaData<T> entityClassMetaData) {
        var fieldsWithParams = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));
        var tableName = entityClassMetaData.getName().toLowerCase();
        var id = entityClassMetaData.getIdField().getName();
        return String.format("update %s set %s where %s = ?", tableName, fieldsWithParams, id);
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

}
