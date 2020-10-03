package com.pzx.algorithm.heap;

import com.sun.org.apache.regexp.internal.RE;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Top Kth问题，找出数组中第K大的元素
 */
public class TopKth {

    /**
     * 小顶堆解决问题
     * 时间复杂度 O(NlogK)
     * 遍历N个元素，最坏情况下每次元素插入堆中的时间复杂度为logK（K为堆的元素个数）
     * @param inputs
     * @param k
     * @return
     */
    private static int minHeap(int[] inputs ,int k){
        if(k == 0 || k > inputs.length)
            throw new IllegalArgumentException("input parameters is illegal!");

        Queue<Integer> queue = new PriorityQueue<>();
        for(int input : inputs){
            if(queue.size() < k)
                queue.offer(input);
            else if (queue.peek() < input){
                    queue.poll();
                    queue.offer(input);
                }

        }
        return queue.poll();
    }

    /**
     * 快排解决问题
     * 时间复杂度为O(N)
     * 时间复杂度推导公式为T（N） = T（N/2） + N（每次遍历处理N个元素，然后问题可以转换成N/2的问题）
     * @param inputs
     * @param k
     * @return
     */
    private static int quickSort(int[] inputs ,int k){
        if(k == 0 || k > inputs.length)
            throw new IllegalArgumentException("input parameters is illegal!");

        int start = 0;
        int end = inputs.length -1;
        while (true){

            int i = start;
            int j = end;
            while (i < j){
                //这里采用已最后一个元素为基准的模式
                //指针首先从左向右移动
                while (i < j && inputs[i] >= inputs[end]) i++;
                while (i < j && inputs[end] >= inputs[j]) j--;
                if(i < j){
                    swap(inputs, i,j);
                }
            }
            swap(inputs, i, end);

            int biggerItemNum = i - start + 1;
            if(biggerItemNum == k)
                return inputs[i];
            else if (biggerItemNum > k){
                end = i -1;
            }else if(biggerItemNum < k){
                k = k - (biggerItemNum);
                start = i + 1;
            }
        }

    }

    private static void swap(int[] inputs, int i, int j){
        if(i != j){
            //交换
            inputs[i] = inputs[i] ^ inputs[j];
            inputs[j] = inputs[i] ^ inputs[j];
            inputs[i] = inputs[i] ^ inputs[j];
        }
    }


    /**
     * 大数据下的topK
     * 对于十亿的字母求，出现字母频率最高的Top Kth
     * map-reduce过程：
     * 1.首先利用hash函数，将字母划分到若干分区之上，保证相同字母在同一分区
     * 2.map过程，在每个分区上求字母频率最高的Top K
     * 3.reduce过程，综合每个分区的Top K，求出所有分区的Top K
     * @param args
     */

    public static void main(String[] args) {
        int[] inputs = new int[]{1,9,8,2,3,5,4,6,7};
        System.out.println(quickSort(inputs,1));
    }

}
