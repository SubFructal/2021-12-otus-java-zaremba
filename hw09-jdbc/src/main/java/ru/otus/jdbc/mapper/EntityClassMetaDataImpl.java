package ru.otus.jdbc.mapper;

import ru.otus.jdbc.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;
    private final String objectClassName;

    public EntityClassMetaDataImpl(Class<T> objectClass) {
        this.objectClassName = objectClass.getSimpleName();
        this.constructor = initConstructor(objectClass);
        this.allFields = Arrays.stream(objectClass.getDeclaredFields()).toList();
        this.idField = initIdField(allFields);
        this.fieldsWithoutId = initIdFieldsWithoutId(allFields);
    }

    private Constructor<T> initConstructor(Class<T> objectClass) {
        try {
            return objectClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Given class doesn't has empty constructor");
        }
    }

    private Field initIdField(List<Field> allFields) {
        return allFields.stream().filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Class fields don't have id field"));
    }

    private List<Field> initIdFieldsWithoutId(List<Field> allFields) {
        return allFields.stream().filter(field -> !field.isAnnotationPresent(Id.class)).toList();
    }

    @Override
    public String getName() {
        return objectClassName;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

}
