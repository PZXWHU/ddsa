package com.pzx.structure.heap;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 二叉堆
 * 完全二叉树+数组实现
 */
public class BinaryHeap<T extends Comparable<? super T>> {

    private static final int DEFAULT_CAPACITY = 10;
    private int currentSize;
    private T[] array;

    public BinaryHeap(){
        this(DEFAULT_CAPACITY);
    }

    public BinaryHeap(int capacity){
        array = (T[])new Comparable[capacity];

    }

    /**
     * 给定数组创建堆有两种方法：
     * heapInsert和heapify，两种时间复杂度均为O（N）
     * @param elements
     */
    public BinaryHeap(T[] elements){
        heapify(elements);
        //heapInsert(elements);
    }

    private void enlargeArray(int capacity){
        array = Arrays.copyOf(array, capacity);
    }

    /**
     * 最坏情况下式logN，平均情况下是常数时间
     * 上滤操作
     * 1、判断是否需要扩容
     * 2、在完全二叉树的下一个位置设置空穴，判断空穴的父节点是否小于当前插入元素
     * 2.1 若大于或等于，则将插入元素储存到空穴中，插入结束
     * 2.2 若小于，则将空穴和父节点互换位置，继续判断空穴是否符合要求
     * @param element
     */
    public void insert(T element){
        if(currentSize == array.length - 1)
            enlargeArray(array.length * 2 + 1);

        array[++currentSize] = element;
        percolateUp(currentSize);
    }

    /**
     * 将索引位置的元素上滤到正确位置
     * @param hole
     */
    private void percolateUp(int hole){
        T element = array[hole];
        //array[0] = element的作用在于当hole = 1时，可以跳出循环，将array[1]即堆顶赋值为插入元素。
        for(array[0] = element;element.compareTo(array[hole / 2]) < 0;hole = hole / 2){
            array[hole] = array[hole / 2];
        }
        array[hole] = element;
    }

    /**
     * 最坏情况下是logN，平均情况下也是logN
     * 1.取堆顶元素用于返回
     * 2.取完全二叉树最后一个元素，放置在堆顶
     * 3.对堆顶元素进行下滤，放置在正确位置。
     *
     * @return
     */
    public T deleteMin(){
        if (currentSize == 0)
            throw new RuntimeException("there is no element in the heap!");
        T min = array[1];
        array[1] = array[currentSize--];
        percolateDown(1);
        return min;
    }


    /**
     * 将索引位置元素下滤到正确的位置
     * @param hole
     */
    private void percolateDown(int hole){
        T element = array[hole];
        int child;
        for(;hole * 2 <= currentSize;hole = child){
            child = hole * 2;
            //选出较小的孩子节点
            if(child != currentSize && array[child + 1].compareTo(array[child]) < 0 )
                child++;
            //如果孩子节点小于下滤元素，则将当前空穴赋值为孩子节点，将空穴下推。否则跳出循环，在当前空穴储存下滤元素
            if(array[child].compareTo(element) < 0)
                array[hole] = array[child];
            else
                break;
        }
        array[hole] = element;
    }


    /**
     * 将所有元素依次插入堆中，相当于对每个元素进行上滤操作
     * 时间复杂度O（NlogN）
     * @param elements
     */
    private void heapInsert(T[] elements){
        for(T element : elements)
            insert(element);
    }

    /**
     * 按任意顺序构建完全二叉树，然后从最后一个非叶子节点一直到根结点进行下滤操作
     * 时间复杂度O（N）
     * @param elements
     */
    private void heapify(T[] elements){
        currentSize = elements.length;
        array = (T[])new Comparable[currentSize + 1];
        int i = 1;
        for(T element : elements){
            array[i++] = element;
        }
        for(int j = currentSize / 2; j > 0; j--){
            percolateDown(j);
        }
    }


}
