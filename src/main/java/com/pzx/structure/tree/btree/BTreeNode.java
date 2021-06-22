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
            maySplit();
        }else {
            BTreeNode<K, V> child = (BTreeNode<K, V>)routeTable.floorEntry(key).getValue();
            child.insert(key, value);
        }
    }

    public void remove(K key){
        if (isLeaf){
            routeTable.remove(key);
            mayMerge();
        }else{
            BTreeNode<K, V> child = (BTreeNode<K, V>)routeTable.floorEntry(key).getValue();
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
        }

        parent.getRouteTable().put(splitNodeKey, splitNode);
        splitNode.next = this.next;
        this.next = splitNode;
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
        K curNodeKey = this.getRouteTable().firstKey();
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
        }
        K rightNodeKey = rightNode.getRouteTable().firstKey();
        leftNode.getParent().getRouteTable().remove(rightNodeKey);
        leftNode.next = rightNode.next;
    }


    private static <K extends Comparable<? super K>, V>  void transferLeftToRight(BTreeNode<K, V> leftNode, BTreeNode<K, V> rightNode){
        //转移数据
        Map.Entry<K, Object> entry = leftNode.getRouteTable().lastEntry();
        leftNode.getRouteTable().remove(entry.getKey());
        K oldRightNodeKey = rightNode.getRouteTable().firstKey();
        rightNode.getRouteTable().put(entry.getKey(), entry.getValue());

        //修改父节点
        rightNode.getParent().getRouteTable().remove(oldRightNodeKey);
        rightNode.getParent().getRouteTable().put(entry.getKey(), rightNode);

    }

    private static <K extends Comparable<? super K>, V>  void transferRightToLeft(BTreeNode<K, V> leftNode, BTreeNode<K, V> rightNode){
        //转移数据
        Map.Entry<K, Object> entry = rightNode.getRouteTable().firstEntry();
        rightNode.getRouteTable().remove(entry.getKey());
        leftNode.getRouteTable().put(entry.getKey(), entry.getValue());

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

    public boolean isLeaf() {
        return isLeaf;
    }

    public BTreeNode<K, V> getNext() {
        return next;
    }

    /*public Map.Entry<K, Object> getMinEntry(){
        return routeTable.firstEntry();
    }

    public Map.Entry<K, Object> getMaxEntry(){
        return routeTable.lastEntry();
    }

    public Map.Entry<K, Object> getNextEntry(K key){
        return routeTable.higherEntry(key);
    }

    public Map.Entry<K, Object> getLastEntry(K key){
        return routeTable.lowerEntry(key);
    }*/


}
