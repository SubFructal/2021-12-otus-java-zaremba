package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final T entity;

    private static final Map<String, Constructor<?>> CONSTRUCTORS = new HashMap<>();
    private static final Map<String, Field> ID_FIELDS = new HashMap<>();
    private static final Map<String, List<Field>> ALL_FIELDS_KITS = new HashMap<>();
    private static final Map<String, List<Field>> FIELDS_WITHOUT_ID_KITS = new HashMap<>();
    private final String inputClassName;

    public EntityClassMetaDataImpl(T object) {
        this.entity = object;
        inputClassName = this.getName();
    }

    @Override
    public String getName() {
        return entity.getClass().getSimpleName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Constructor<T> getConstructor() {
        if (containsInPrepared(CONSTRUCTORS, inputClassName)) {
            return (Constructor<T>) getFromPreparedConstructors(inputClassName);
        }
        var newConstructor = Arrays.stream(entity.getClass().getConstructors())
                .filter(constructor -> constructor.getParameterCount() == 0).toList().get(0);
        addToPrepared(inputClassName, newConstructor);
        return (Constructor<T>) newConstructor;
    }

    @Override
    public Field getIdField() {
        if (containsInPrepared(ID_FIELDS, inputClassName)) {
            return getFromPreparedIdFields(inputClassName);
        }
        var newIdField = getAllFields().stream()
                .filter(field -> field.isAnnotationPresent(Id.class)).toList().get(0);
        addToPrepared(inputClassName, newIdField);
        return newIdField;
    }

    @Override
    public List<Field> getAllFields() {
        if (containsInPrepared(ALL_FIELDS_KITS, inputClassName)) {
            return getFromPreparedFieldsKits(ALL_FIELDS_KITS, inputClassName);
        }
        var newAllFieldsKit = List.of(entity.getClass().getDeclaredFields());
        addToPrepared(ALL_FIELDS_KITS, inputClassName, newAllFieldsKit);
        return newAllFieldsKit;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (containsInPrepared(FIELDS_WITHOUT_ID_KITS, inputClassName)) {
            return getFromPreparedFieldsKits(FIELDS_WITHOUT_ID_KITS, inputClassName);
        }
        var newFieldsWithoutId = getAllFields().stream()
                .filter(field -> !field.isAnnotationPresent(Id.class)).toList();
        addToPrepared(FIELDS_WITHOUT_ID_KITS, inputClassName, newFieldsWithoutId);
        return newFieldsWithoutId;
    }

    private boolean containsInPrepared(Map<String, ?> preparedKits, String inputClassName) {
        return preparedKits.containsKey(inputClassName);
    }

    private void addToPrepared(Map<String, List<Field>> preparedEntities, String inputClassName, List<Field> entity) {
        preparedEntities.put(inputClassName, entity);
    }

    private void addToPrepared(String inputClassName, Field entity) {
        ID_FIELDS.put(inputClassName, entity);
    }

    private void addToPrepared(String inputClassName, Constructor<?> entity) {
        CONSTRUCTORS.put(inputClassName, entity);
    }

    private List<Field> getFromPreparedFieldsKits(Map<String, List<Field>> preparedEntities, String inputClassName) {
        return preparedEntities.get(inputClassName);
    }

    private Field getFromPreparedIdFields(String inputClassName) {
        return ID_FIELDS.get(inputClassName);
    }

    private Constructor<?> getFromPreparedConstructors(String inputClassName) {
        return CONSTRUCTORS.get(inputClassName);
    }

//    public static void main(String[] args) {
//        var first = new EntityClassMetaDataImpl<>(new Client());
//        var second = new EntityClassMetaDataImpl<>(new Client("test"));
//        var second2 = new EntityClassMetaDataImpl<>(new Client(12l, "test"));
//        var third = new EntityClassMetaDataImpl<>(new Manager());
//        var forth = new EntityClassMetaDataImpl<>(new Manager(1l, "label", "name"));
//        var fifth = new EntityClassMetaDataImpl<>(new Manager("label2"));
//
//        System.out.println(first.inputClassName);
//        System.out.println(second.inputClassName);
//        System.out.println(second2.inputClassName);
//        System.out.println(third.inputClassName);
//        System.out.println(forth.inputClassName);
//        System.out.println(fifth.inputClassName);
//
//        first.getConstructor();
//        second.getConstructor();
//        second2.getConstructor();
//
//        third.getConstructor();
//        forth.getConstructor();
//        fifth.getConstructor();
//
//        first.getIdField();
//        second.getIdField();
//        second2.getIdField();
//        third.getIdField();
//        forth.getIdField();
//        fifth.getIdField();
//
//        System.out.println(CONSTRUCTORS);
//        System.out.println(ID_FIELDS);
//    }
}
