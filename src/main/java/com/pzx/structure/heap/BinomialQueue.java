package com.pzx.structure.heap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 左式堆和斜堆都能以0（logN）支持合并、插入、deleteMin，二叉堆以平均常数时间执行插入
 * 所以引入二项队列，每次操作最坏时间O（logN），插入平均时间为常数
 *
 * 二项树森林
 * 二项树：相同二项树合并时，将具有较大堆顶的二项树作为具有较小堆顶的孩子
 * 由于二项树具有多个孩子，所以使用兄弟孩子表示
 * 具有N个元素的二项队列，最多具有logN个二项树
 */
public class BinomialQueue<T extends Comparable<? super T>> {

    private int currentSize;
    private BinomialTreeNode<T>[] theTrees;
    private final static int DEFAULT_TREES = 1;

    public BinomialQueue(){
        theTrees = new BinomialTreeNode[DEFAULT_TREES];
    }

    public BinomialQueue(int capacity){
        theTrees = new BinomialTreeNode[(int)Math.ceil(Math.log(capacity + 1)/Math.log(2))];
    }

    public static <T extends Comparable<? super T>> BinomialQueue<T> singleElementBinomialQueue(T element){
        BinomialQueue<T> binomialQueue = new BinomialQueue<>();
        binomialQueue.theTrees[0] = new BinomialTreeNode<>(element,null,null);
        binomialQueue.currentSize = 1;
        return binomialQueue;
    }


    public void insert(T element){
        merge(singleElementBinomialQueue(element));
    }

    /**
     * 1.首先找到具有最小根的二项树，将其在森林中删除。
     * 2.对于该二项树，删除堆顶，将剩下的部分组合成一个新的森林
     * 3.合并两个森林
     *
     */
    public T deleteMin(){
        if(currentSize == 0)
            throw new RuntimeException("当前堆为空");
        int minIndex = -1;
        for(int i = 0; i < theTrees.length; i++){
            if (theTrees[i] == null)
                continue;
            if(minIndex < 0 || theTrees[minIndex].element.compareTo(theTrees[i].element) > 0)
                minIndex = i;
        }

        BinomialTreeNode<T> deletedTree = theTrees[minIndex].leftChild;

        BinomialQueue<T> deletedQueue = new BinomialQueue<>((1 << minIndex) - 1);
        deletedQueue.currentSize = (1 << minIndex) - 1;
        for(int i = minIndex - 1; i >=0 ; i--){
            deletedQueue.theTrees[i] = deletedTree;
            deletedTree = deletedTree.nextSibling;
            deletedQueue.theTrees[i].nextSibling = null;
        }

        T minElement = theTrees[minIndex].element;
        theTrees[minIndex] = null;
        currentSize -= deletedQueue.currentSize + 1;

        merge(deletedQueue);
        return minElement;
    }



    /**
     * 时间复杂度 logN
     * 1、判断是否需要扩容，是则将二项树数组扩大为合并的二项树数组中较大的长度+1
     * 2、依次对两个二项树数组相同位置的元素合并，储存到扩容后的数组。每个位置合并之后，将给下一个合并位置一个传递变量
     * 2.1、如果两个元素均为null，则将扩容数组相应位置赋值为上一个位置的传递变量。将当前位置的传递变量赋值为null
     * 2.2、如果两个元素均不为null，则上两个元素合并，赋值为当前位置的传递变量。上一个位置的传递变量赋值到扩容数组。
     * 2.3、如果两个元素一个为null，一个不为null，则如果上一个位置的传递变量为null，则将不为null的元素赋值到扩容数组,当前传递位置的元素为null。
     *                                           否则合并上一个位置的传递变量和不为null的元素，作为当前位置的传递变量，当前位置扩容数组赋值为null。
     * 3、当遍历完较短数组后，继续遍历扩容数组，进行类似处理。
     * @param rhs
     */
    public void merge(BinomialQueue<T> rhs){
        if (rhs == null || this == rhs)
            return;
        currentSize += rhs.currentSize;

        //扩容，保证合并之后不会超出容量限制
        if(currentSize > capacity()){
            int maxLength = Math.max(theTrees.length, rhs.theTrees.length);
            expandTree(maxLength + 1);
        }

        BinomialTreeNode<T> carry = null;
        //因为之前已经扩容，所以当前对象的theTree数组不会越界
        for(int i = 0; i < rhs.theTrees.length; i++){
            if(theTrees[i] == null && rhs.theTrees[i] == null){
                theTrees[i] = carry;
                carry = null;
            }else if(theTrees[i] != null && rhs.theTrees[i] != null){
                BinomialTreeNode<T> tmp = carry;
                carry = combineTrees(theTrees[i], rhs.theTrees[i]);
                theTrees[i] = tmp;
            }else {
                theTrees[i] = theTrees[i] == null ? rhs.theTrees[i] : theTrees[i];
                if(carry != null){
                    carry = combineTrees(carry, theTrees[i]);
                    theTrees[i] = null;
                }
            }
        }
        for(int i = rhs.theTrees.length; i < theTrees.length; i++){
            if (carry == null)
                break;
            else if(theTrees[i] == null){
                theTrees[i] = carry;
                break;
            }else {
                carry = combineTrees(carry,theTrees[i]);
                theTrees[i] = null;
            }
        }

    }


    /**
     * 合并相同大小的二项树,将具有较大堆顶的堆作为具有较小堆顶的堆的孩子。
     * @param t1
     * @param t2
     * @return
     */
    private BinomialTreeNode<T> combineTrees(BinomialTreeNode<T> t1, BinomialTreeNode<T> t2){
        if(t1 == null && t2 == null) return null;

        if(t1.element.compareTo(t2.element) > 0){
            BinomialTreeNode<T> tmp = t1;
            t1 = t2;
            t2 = tmp;
        }
        t2.nextSibling = t1.leftChild;
        t1.leftChild = t2;
        return t1;
    }

    private int capacity(){
        return (1 << theTrees.length) - 1;
    }

    private void expandTree(int newLength){
        theTrees = Arrays.copyOf(theTrees, newLength);
    }

    private static class BinomialTreeNode<T>{
        private T element;
        private BinomialTreeNode<T> leftChild;
        private BinomialTreeNode<T> nextSibling;

        public BinomialTreeNode(T element, BinomialTreeNode<T> leftChild, BinomialTreeNode<T> nextSibling) {
            this.element = element;
            this.leftChild = leftChild;
            this.nextSibling = nextSibling;
        }
    }

    public static void main(String[] args) {
        BinomialQueue<Integer> heap = new BinomialQueue<>();
        for(int i = 0; i< 100; i++){
            heap.insert(ThreadLocalRandom.current().nextInt(1000));
        }
        BinomialQueue<Integer> heap1 = new BinomialQueue<>();
        for(int i = 0; i< 100; i++){
            heap1.insert(ThreadLocalRandom.current().nextInt(1000));
        }
        heap.merge(heap1);
        for(int i =0; i < 200; i++){
            System.out.println(heap.deleteMin());
        }
    }

}
