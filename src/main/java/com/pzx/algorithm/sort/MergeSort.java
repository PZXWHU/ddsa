package com.pzx.algorithm.sort;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 归并排序:时间复杂度O（NlogN），空间复杂度O（N）
 * 归并排序具有最少的比较次数，但是具有较多的数据移动次数。
 * 快速排序具有较少的数据移动次数，但是具有较多的比较次数
 *
 */
public class MergeSort {

    /**
     * 类似动态规划的想法
     * 从下向大推导，先合并较小的序列（首次合并两个单个元素，因为单个元素一定是有序的）
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void iterativeMergeSort(T[] a){
        T[] tmpArray = (T[])new Comparable[a.length];
        for(int i = 1; i < a.length; i *= 2){
            for(int j = 0; j + i < a.length; j += i*2){
                merge(a,tmpArray,j,j + i, Math.min(j + i * 2 - 1,a.length - 1) );
            }
        }
    }


    public static <T extends Comparable<? super T>> void mergeSort(T[] a){
        T[] tmpArray = (T[])new Comparable[a.length];
        mergeSort(a,tmpArray,0,a.length-1);
    }

    /**
     * 归并排序的递归写法
     * @param a
     * @param tmpArray
     * @param start
     * @param end
     * @param <T>
     */
    private static <T extends Comparable<? super T>> void mergeSort(T[] a,T[] tmpArray, int start, int end){

        if(start < end){
            int center = (start + end) / 2;
            mergeSort(a,tmpArray,start,center);
            mergeSort(a,tmpArray,center + 1,end);
            merge(a,tmpArray,start,center + 1, end);
        }
    }



    private static <T extends Comparable<? super T>> void merge(T[] a,T[] tmpArray, int leftPos, int rightPos, int rightEnd){
        int leftEnd = rightPos - 1;
        int tmpPos = leftPos;int leftStart = leftPos;
        while (leftPos <= leftEnd && rightPos <= rightEnd){
            if(a[leftPos].compareTo(a[rightPos]) < 0)
                tmpArray[tmpPos++] = a[leftPos++];
            else
                tmpArray[tmpPos++] = a[rightPos++];
        }

        while (leftPos <= leftEnd)
            tmpArray[tmpPos++] = a[leftPos++];

        while (rightPos <= rightEnd)
            tmpArray[tmpPos++] = a[rightPos++];

        for(int i = leftStart; i <= rightEnd; i++)
            a[i] = tmpArray[i];

    }

}
