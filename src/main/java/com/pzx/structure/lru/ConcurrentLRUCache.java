package com.pzx.structure.lru;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentLRUCache<K, V> implements LRUCache<K, V> {

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void set(K key, V value) {

    }
}
