package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final T entity;

    public EntityClassMetaDataImpl(T object) {
        this.entity = object;
    }

    @Override
    public String getName() {
        return entity.getClass().getSimpleName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Constructor<T> getConstructor() {
        return (Constructor<T>) Arrays.stream(entity.getClass().getConstructors())
                .filter(constructor -> constructor.getParameterCount() == getAllFields().size()).toList().get(0);
    }

    @Override
    public Field getIdField() {
        return getAllFields().stream().filter(field -> field.isAnnotationPresent(Id.class)).toList().get(0);
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(entity.getClass().getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream().filter(field -> !field.isAnnotationPresent(Id.class)).toList();
    }
}
