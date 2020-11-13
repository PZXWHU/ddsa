package com.pzx.structure.lru;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomLRUCache<K, V> implements LRUCache<K, V> {

    private final Map<K, Entry<K, V>> cache;

    //双向链表，最近访问的元素在head端
    private final Entry<K, V> head;
    private final Entry<K, V> tail;

    private final int MAX_CAPACITY;

    public CustomLRUCache(int initCapacity, int maxCapacity){
        MAX_CAPACITY = maxCapacity;
        cache = new HashMap<>(initCapacity);
        head = tail = new Entry<>(null,null);
        head.after = tail;
        tail.before = head;

    }

    @Override
    public void set(K key, V value) {
        Entry<K, V> entry;
        if ((entry = cache.get(key)) != null){
            entry.value = value;
            moveEntryAfterHead(entry);
        }else {
            entry = new Entry<K, V>(key, value);
            addEntryAfterHead(entry);
            cache.put(key, entry);


            if (size() > MAX_CAPACITY){
                Entry<K, V> deletedEntry = tail.before;
                removeEntryFromDeque(deletedEntry);
                cache.remove(deletedEntry.key);
            }
        }
    }

    @Override
    public V get(K key) {
        Entry<K, V> entry;
        if ((entry = cache.get(key)) != null){
            moveEntryAfterHead(entry);
            return entry.value;
        }
        return null;
    }

    private int size(){
        return cache.size();
    }

    /**
     * 将双向列表中的节点移动到head节点之后
     * @param entry
     */
    private void moveEntryAfterHead(Entry<K, V> entry){
        removeEntryFromDeque(entry);
        addEntryAfterHead(entry);
    }

    /**
     * 从双向列表中删除节点
     * @param entry
     */
    private void removeEntryFromDeque(Entry<K, V> entry){
        entry.before.after = entry.after;
        entry.after.before = entry.before;
    }

    /**
     * 在head节点后添加新节点
     * @param entry
     */
    private void addEntryAfterHead(Entry<K, V> entry){
        entry.before = head;
        entry.after = head.after;
        head.after.before = entry;
        head.after = entry;
    }

    private class Entry<K, V>{
        private K key;
        private V value;
        private Entry<K, V> before;
        private Entry<K ,V> after;

        public Entry(K key, V value){
            this(key, value, null, null);
        }

        public Entry(K key, V value, Entry<K ,V> before, Entry<K, V> after) {
            this.key = key;
            this.value = value;
            this.before = before;
            this.after = after;
        }


    }
}
