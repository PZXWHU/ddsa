package com.pzx.algorithm;

import java.awt.image.SampleModel;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Sort {

    /**
     * 指针交换法
     * 快速排序递归写法,平均时间复杂度O（NlogN），最坏O（N^2）
     *
     * 1.选择数组第一个元素为基准元素
     * 2.右指针从右向左遍历，直到遇到小于基准元素
     * 3.左指针从左向右遍历，直到遇到大于基准元素
     * 4.交换两元素位置
     * 5.重复2、3、4步，直到两指针相遇
     * 6.两指针相遇处左边所有元素小于等于基准元素，右边所有元素大于等于基准元素，相遇处元素必然小于基准元素，所以将相遇位置的元素和基准元素交换位置
     * 7.对相遇位置的左子数组和有子数组进行上述同样的操作
     * @param inputs
     * @param start
     * @param end
     * @return
     */
    private static void quickSort(int[] inputs ,int start, int end){
        if (inputs.length == 0 || start >= end)
            return ;

        int i = start + 1;
        int j = end;
        while (i <= j){
            while (i <= j && inputs[start] < inputs[j]) j--;
            while (i <= j && inputs[i] < inputs[start]) i++;
            if(i <= j)
                swap(inputs, i++, j--);
        }
        //指针相遇处的元素肯定小于基准元素，进行交换
        swap(inputs,start, j);

        quickSort(inputs, start, j -1);
        quickSort(inputs,j+1, end);

    }

    /**
     * 挖坑填数法
     *
     * @param inputs
     * @param start
     * @param end
     */
    private static void quickSort1(int[] inputs ,int start, int end){
        if (inputs.length == 0 || start >= end)
            return ;

        int i = start;
        int j = end;

        while (i < j){
            while (i < j && inputs[start] < inputs[j]) j--;
            if(i < j){
                inputs[i++] = inputs[j];
            }
            while (i < j && inputs[i] < inputs[start]) i++;
            if(i < j){
                inputs[j--] = inputs[i];
            }
        }

        inputs[j] = inputs[start];

        quickSort1(inputs, start, i -1);
        quickSort1(inputs,i+1, end);
    }


    /**
     *非递归快速排序
     * 利用栈记录要排序的范围段，依次出栈进行处理
     * @param inputs
     * @param start
     * @param end
     */
    private static void quickSortIteratively(int[] inputs ,int start, int end){
        if (inputs.length == 0 || start >= end)
            return ;

        Deque<Integer> stack = new LinkedList<>();
        stack.push(start);stack.push(end);
        while (!stack.isEmpty())
        {
            end = stack.poll();start = stack.poll();
            int i = start;int j = end;
            while (i < j){
                while (i < j && inputs[start] <= inputs[j]) j--;
                while (i < j && inputs[i] <= inputs[start]) i++;
                if(i < j){
                    swap(inputs, i, j);
                }
            }
            //指针相遇处的元素肯定小于基准元素，进行交换
            swap(inputs, start, j);
            if(start < j-1){
                stack.push(start);
                stack.push(j-1);
            }
            if(j+1 < end){
                stack.push(j+1);
                stack.push(end);
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


    public static void main(String[] args) {
        int[] inputs = new int[101];
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

        for (int i = 0; i<101; i++){
            inputs[i] = threadLocalRandom.nextInt(100);
        }

        quickSort1(inputs, 0 ,inputs.length -1);
        System.out.println(Arrays.toString(inputs));
        com.pzx.algorithm.sort.Sort.checkSort(inputs);
    }



}
