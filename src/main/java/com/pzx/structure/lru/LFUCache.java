package com.pzx.structure.lru;

import java.util.*;

public class LFUCache {

    Map<Integer, Node> cache;
    Map<Integer, LinkedHashSet<Node>> nodeMap;
    int capacity;
    int size;
    int min;


    class Node{
        int key;
        int value;
        int count;

        public Node(int key, int value, int count){
            this.key = key;
            this.value = value;
            this.count = count;
        }
    }

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.min = 0;
        this.cache = new HashMap<>();
        this.nodeMap = new HashMap<>();

    }

    public int get(int key) {
        if(!cache.containsKey(key)) return -1;

        Node node = cache.get(key);
        nodeInc(node);

        return node.value;


    }

    public void put(int key, int value) {
        if(capacity == 0) return;

        if(cache.containsKey(key)){
            Node node = cache.get(key);
            node.value = value;
            nodeInc(node);
        }else{
            if(size == capacity){
                LinkedHashSet<Node> set = nodeMap.get(min);
                Node remove = set.iterator().next();
                set.remove(remove);
                cache.remove(remove.key);
                size--;
            }
            Node node = new Node(key, value, 1);
            cache.put(key, node);
            nodeMap.putIfAbsent(1, new LinkedHashSet<>());
            nodeMap.get(1).add(node);
            min = 1;
            size++;
        }
    }

    private void nodeInc(Node node){
        LinkedHashSet set = nodeMap.get(node.count);
        set.remove(node);
        if(min == node.count && set.size() == 0)
            min += 1;
        node.count++;
        nodeMap.putIfAbsent(node.count, new LinkedHashSet<Node>());
        nodeMap.get(node.count).add(node);

    }

}
