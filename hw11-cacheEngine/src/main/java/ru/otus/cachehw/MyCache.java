package ru.otus.cachehw;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private static final String PUT_ACTION = "put to cache";
    private static final String REMOVE_ACTION = "remove from cache";
    private static final String GET_ACTION = "get from cache";

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        useListeners(key, value, PUT_ACTION);
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        useListeners(key, value, REMOVE_ACTION);
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        useListeners(key, value, GET_ACTION);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        } else {
            throw new UnsupportedOperationException("executeRemove error");
        }
    }

    private void useListeners(K key, V value, String action) {
        if (!listeners.isEmpty()) {
            listeners.forEach(listener -> listener.notify(key, value, action));
        }
    }

}
