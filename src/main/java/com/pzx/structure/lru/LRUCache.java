package com.pzx.structure.lru;

public interface LRUCache<K, V> {

    void set(K key, V value);

    V get(K key);

}
