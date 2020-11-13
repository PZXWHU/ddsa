package com.pzx.structure.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapLRUCache<K, V> implements LRUCache<K, V> {

    private final int MAX_CAPACITY;
    private final Map<K,V> cache;

    public LinkedHashMapLRUCache(int initCapacity, int maxCapacity) {
        this.MAX_CAPACITY = maxCapacity;
        cache = new LinkedHashMap<K, V>(initCapacity, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > MAX_CAPACITY;
            }
        };
    }

    @Override
    public void set(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }
}
