package com.pzx.algorithm;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 最大子序列和问题
 * 问题描述：给定整数数组（元素可能为负，求最大的子序列和，如果全为负数，则输出0）
 * 例如：输入-2，11，-4，13，-5，-2  输入20（从11到13）
 *
 * @author PZX
 * 2020.6.19
 */
public class MaxSubSequenceSum {

    /**
     * 算法1：使用递归二分法解决问题  时间复杂度O（NlogN）
     *
     * 算法原理：
     * 最大的子序列和对应的子序列可能出现在三个位置：1、左半边数组 2、右半边数组 3、横跨左右半边数组
     * 如果在左半边数组或者右半边数组，则递归调用函数。
     * 如果横跨左右半边数组，则对应的子序列应为 包括左半边数组最后一个元素的最大子序列和 + 包括右半边数组第一个元素的最大子序列和
     *
     * 算法过程：
     * 1.基准情况：当数组只包含一个元素时，如果为正数则返回，为负数则返回0
     * 2.调用自身获取左半边数组和右半边函数的最大子序列和，求横跨左右半边数组对应的最大子序列和
     * 3.比较上述三种方式的结果，即可得到最大子序列和
     *
     *
     *
     * @param inputArray
     * @param left
     * @param right
     * @return
     */
    private static int MaxSubSequenceSumRec(int[] inputArray, int left, int right){

        if(left == right){
            return inputArray[left] > 0 ? inputArray[left] : 0;
        }

        int center = (left + right) / 2;
        int leftArrayMaxSum = MaxSubSequenceSumRec(inputArray, left, center);
        int rightArrayMaxSum = MaxSubSequenceSumRec(inputArray, center+1, right);

        //求包括左半边数组最后一个元素的最大子序列和
        int currentSum = 0; int leftHalfMaxSum = 0;
        for(int i = center; i>=left; i--){
            currentSum += inputArray[i];
            if(currentSum > leftHalfMaxSum){
                leftHalfMaxSum = currentSum;
            }
        }

        //求包括右半边数组第一个元素的最大子序列和
        currentSum = 0;int rightHalfMaxSum = 0;
        for(int i = center + 1; i<=right; i++){
            currentSum += inputArray[i];
            if(currentSum > rightHalfMaxSum){
                rightHalfMaxSum = currentSum;
            }
        }

        return max3(leftArrayMaxSum, rightArrayMaxSum, leftHalfMaxSum + rightHalfMaxSum);
    }

    private static int max3(int x, int y,int z){
        return x > y ? (x > z ? x : z) : (y > z ? y : z);
    }

    /**
     * 算法2：线性扫描跳跃法  时间复杂度 0（N）
     *
     * 算法原理：
     * 1.最大子序列和对应的子序列的前缀序列的和不可能为负数，例如2，-11，4，13，前缀序列2，-11和为负数，其和必小于除去负前缀序列后的情况，即4，13
     * 2.最大子序列和对应的子序列的邻接的任意前序列的和不可能为整数。10，-5，3，5，邻接的的前序列10，-5，所以以3开头的子序列不可能为最大子序列
     *
     * 算法过程：
     * 1.线性扫描，将数组中的数累计相加，记录下累计和的最大值。
     * 2.如果某次累计相加后和小于0，则将累计和赋值为0，继续遍历。
     *
     * 解释：默认子序列头部在数组开头，将累计和赋值为0的意义在于，将子序列的头部直接移动到累计和小于0的序列后,
     * 因为最大子序列的头部不可能位于累计序列和小于0所对应的序列中，因为该序列的除了最后一个元素，前缀和都大于0
     * @param inputArray
     * @return
     */
    private static int MaxSubSequenceSumLin(int[] inputArray){

        //int head = 0; int tail = -1;int currentHead = 0; //需要输出最大子序列时使用
        int maxSum = 0;int currentSum = 0;
        for(int i = 0; i < inputArray.length; i++){
            currentSum += inputArray[i];
            if (currentSum > maxSum){
                maxSum = currentSum;
                //head = currentHead;//移动头指针
                //tail = i;//移动尾指针 //移动尾指针
            }else if (currentSum <= 0){
                currentSum = 0;
                //currentHead = i + 1;
            }
        }

        /*
        for(int i = head; i<=tail; i++){
            System.out.print(inputArray[i] + " ");
        }
         */

        return maxSum;
    }

    public static void main(String[] args) {
        int[] inputArray = new int[100];
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(int i = 0; i<100; i++){
            inputArray[i] = random.nextInt();
        }
        System.out.println(MaxSubSequenceSumRec(inputArray,0, inputArray.length-1));
        System.out.println(MaxSubSequenceSumLin(inputArray));
    }
}
