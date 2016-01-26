package io.github.marcinn.common;

public interface TimeoutCache<K, V> {

    V get(K key);

    void put(K key, V obj, int timeout);

    void remove(K key);

    int size();
}
