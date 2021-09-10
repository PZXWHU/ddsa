package com.pzx.structure.tree.btree;

import com.pzx.structure.lru.ConcurrentLRUCache;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * B+树节点
 * @param <K>
 * @param <V>
 */
public class BTreeNode<K extends Comparable<? super K>, V> {

    //B+树阶数
    private final int m;

    private final NavigableMap<K, Object> routeTable;

    private BTreeNode<K, V> parent;

    private BTreeNode<K, V> next;

    private final boolean isLeaf;

    public BTreeNode(int m, boolean isLeaf, BTreeNode<K, V> parent) {
        this.m = m;
        this.isLeaf = isLeaf;
        this.parent = parent;
        this.next = null;
        this.routeTable = new ConcurrentSkipListMap<>();
    }

    public void insert(K key, V value){
        if (isLeaf){
            routeTable.put(key, value);
            mayUpdateKey(true);
            maySplit();
        }else {
            Map.Entry<K, Object> entry = routeTable.floorEntry(key);
            BTreeNode<K, V> child = (BTreeNode<K, V>)(entry == null ? routeTable.firstEntry().getValue() : entry.getValue());
            child.insert(key, value);
        }
    }

    public void mayUpdateKey(boolean isInsert){
        if (parent != null && routeTable.size() != 0){
            K key = routeTable.firstKey();
            Map.Entry<K, Object> entry = isInsert ? parent.getRouteTable().ceilingEntry(key) : parent.getRouteTable().floorEntry(key);
            if (entry == null){
                System.out.println(parent.getRouteTable() + "  " + routeTable);
            }
            if (entry == null || entry.getKey().compareTo(key) != 0){
                parent.getRouteTable().remove(entry.getKey());
                parent.getRouteTable().put(key, this);
                parent.mayUpdateKey(isInsert);
            }
        }
    }

    public void remove(K key){
        if (isLeaf){
            routeTable.remove(key);
            mayUpdateKey(false);
            mayMerge();
        }else{
            Map.Entry<K, Object> entry = routeTable.floorEntry(key);
            if (entry == null) return;
            BTreeNode<K, V> child = (BTreeNode<K, V>)entry.getValue();
            child.remove(key);
        }
    }



    public void maySplit(){
        if (routeTable.size() > m)
            split();
    };

    public void split(){
        if (parent == null){
            parent = new BTreeNode<>(m , false, null);
            parent.getRouteTable().put(routeTable.firstKey(), this);
        }else if (brotherHelpSplitting()){
            return;
        }

        splitNode();
    }

    private void splitNode(){

        BTreeNode<K, V> splitNode = new BTreeNode<>(m, isLeaf, parent);
        int size = routeTable.size();
        int i = 0;
        K splitNodeKey = null;
        Iterator<Map.Entry<K, Object>> iterator = routeTable.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<K, Object> entry = iterator.next();
            if (i++ < size / 2)
                continue;
            iterator.remove();
            if (splitNodeKey == null) splitNodeKey = entry.getKey();
            splitNode.getRouteTable().put(entry.getKey(), entry.getValue());
            if (entry.getValue() instanceof BTreeNode){
                ((BTreeNode) entry.getValue()).parent = splitNode;
            }
        }

        parent.getRouteTable().put(splitNodeKey, splitNode);
        if (isLeaf){
            splitNode.next = this.next;
            this.next = splitNode;
        }
        parent.maySplit();
    }

    public void mayMerge(){
        if (parent != null && routeTable.size() < m / 2)
            merge();

    }

    public void merge(){
        if (brotherHelpMerging())
            return;
        mergeNode();

    };

    private void mergeNode(){
        BTreeNode<K, V> leftBrotherNode = getLeftBrotherNode();
        BTreeNode<K, V> rightBrotherNode = getRightBrotherNode();
        if (leftBrotherNode == null && rightBrotherNode == null){
            throw new RuntimeException("impossible error!");
        }else{
            if (leftBrotherNode != null)
                mergeLeftAndRight(leftBrotherNode, this);
            else
                mergeLeftAndRight(this, rightBrotherNode);
        }
        parent.mayMerge();
    }


    public boolean brotherHelpSplitting(){

        K curNodeKey = this.getRouteTable().firstKey();
        BTreeNode<K, V> leftBrotherNode = getLeftBrotherNode();
        BTreeNode<K, V> rightBrotherNode = getRightBrotherNode();
        if (leftBrotherNode != null && leftBrotherNode.getRouteTable().size() < this.getM()){
            transferRightToLeft(leftBrotherNode, this);
            return true;
        }
        if (rightBrotherNode != null && rightBrotherNode.getRouteTable().size() < this.getM()){
            transferLeftToRight(this, rightBrotherNode);
            return true;
        }
        return false;

    }

    public boolean brotherHelpMerging(){
        K curNodeKey = this.getRouteTable().firstKey();
        BTreeNode<K, V> leftBrotherNode = getLeftBrotherNode();
        BTreeNode<K, V> rightBrotherNode = getRightBrotherNode();
        if (leftBrotherNode != null && leftBrotherNode.getRouteTable().size() > this.getM() / 2){
            transferLeftToRight(leftBrotherNode, this);
            return true;
        }
        if (rightBrotherNode != null && rightBrotherNode.getRouteTable().size() > this.getM() / 2){
           transferRightToLeft(this, rightBrotherNode);
            return true;
        }
        return false;

    }

    private static <K extends Comparable<? super K>, V>  void mergeLeftAndRight(BTreeNode<K, V> leftNode, BTreeNode<K, V> rightNode){

        for(Map.Entry<K, Object> entry : rightNode.getRouteTable().entrySet()){
            leftNode.getRouteTable().put(entry.getKey(), entry.getValue());
            if (entry.getValue() instanceof BTreeNode){
                ((BTreeNode) entry.getValue()).parent = leftNode;
            }
        }
        K rightNodeKey = rightNode.getRouteTable().firstKey();
        leftNode.getParent().getRouteTable().remove(rightNodeKey);

        if (leftNode.isLeaf() && rightNode.isLeaf())
            leftNode.next = rightNode.next;
    }


    private static <K extends Comparable<? super K>, V>  void transferLeftToRight(BTreeNode<K, V> leftNode, BTreeNode<K, V> rightNode){
        //转移数据
        Map.Entry<K, Object> entry = leftNode.getRouteTable().lastEntry();
        leftNode.getRouteTable().remove(entry.getKey());
        K oldRightNodeKey = rightNode.getRouteTable().firstKey();
        rightNode.getRouteTable().put(entry.getKey(), entry.getValue());

        //修改子节点
        if (entry.getValue() instanceof BTreeNode){
            ((BTreeNode) entry.getValue()).parent = rightNode;
        }

        //修改父节点
        rightNode.getParent().getRouteTable().remove(oldRightNodeKey);
        rightNode.getParent().getRouteTable().put(entry.getKey(), rightNode);

    }

    private static <K extends Comparable<? super K>, V>  void transferRightToLeft(BTreeNode<K, V> leftNode, BTreeNode<K, V> rightNode){
        //转移数据
        Map.Entry<K, Object> entry = rightNode.getRouteTable().firstEntry();
        rightNode.getRouteTable().remove(entry.getKey());
        leftNode.getRouteTable().put(entry.getKey(), entry.getValue());

        //修改子节点
        if (entry.getValue() instanceof BTreeNode){
            ((BTreeNode) entry.getValue()).parent = leftNode;
        }

        //修改父节点
        K newRightNodeKey = rightNode.getRouteTable().firstKey();
        rightNode.getParent().getRouteTable().remove(entry.getKey());
        rightNode.getParent().getRouteTable().put(newRightNodeKey, rightNode);


    }

    public BTreeNode<K, V> getLeftBrotherNode(){
        K curNodeKey = this.getRouteTable().firstKey();
        Map.Entry<K, Object> entry = this.getParent().getRouteTable().lowerEntry(curNodeKey);
        if (entry != null)
            return (BTreeNode<K, V>)entry.getValue();
        else
            return null;
    }

    public BTreeNode<K, V> getRightBrotherNode(){
        K curNodeKey = this.getRouteTable().firstKey();
        Map.Entry<K, Object> entry = this.getParent().getRouteTable().higherEntry(curNodeKey);
        if (entry != null)
            return (BTreeNode<K, V>)entry.getValue();
        else
            return null;
    }

    public NavigableMap<K, Object> getRouteTable() {
        return routeTable;
    }

    public int getM() {
        return m;
    }

    public BTreeNode<K, V> getParent() {
        return parent;
    }

    public void setParent(BTreeNode<K, V> parent) {
        this.parent = parent;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public BTreeNode<K, V> getNext() {
        return next;
    }

}
