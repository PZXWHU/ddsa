package com.pzx.algorithm.sort;

/**
 * 堆排序：时间复杂度O（NlogN）
 *1、首先对于数组建立大顶堆
 *2、删除堆顶元素，则堆需要进行调整，将堆的最后一个元素放置堆顶进行下滤。所以可将删除的堆顶元素放置当前堆的最后一个元素的位置。将堆的大小减1.
 */
public class HeapSort {

    /**
     * 向下过滤
     * @param a
     * @param hole
     * @param size
     * @param <T>
     */
    private static <T extends Comparable<? super T>> void percolateDown(T[] a, int hole, int size){
        T tmp = a[hole];
        int child;
        for(;hole * 2 + 1 < size; hole = child){
            child = hole * 2 + 1;
            if(child != size -1 && a[child].compareTo(a[child + 1]) < 0)
                child++;
            if(tmp.compareTo(a[child]) < 0)
                a[hole] = a[child];
            else
                break;
        }
        a[hole] = tmp;
    }

    public static <T extends Comparable<? super T>> void heapSort(T[] a){
        //建立堆
        for(int i = a.length / 2 - 1; i >=0; i--){
            percolateDown(a,i,a.length);
        }
        //将堆中最大值和数组末端元素交换，然后下滤
        for(int i = a.length - 1; i >= 0; i--){
            T tmp = a[i];
            a[i] = a[0];
            a[0] = tmp;
            percolateDown(a,0,i);
        }
    }


}
