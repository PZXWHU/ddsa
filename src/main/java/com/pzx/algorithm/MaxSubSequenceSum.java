package com.pzx.algorithm;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 最大子序列和问题
 * 问题描述：给定整数数组（元素可能为负，求最大的子序列和）
 * 例如：输入-2，11，-4，13，-5，-2  输入20（从11到13）
 *
 * 最大子矩阵和问题
 * 问题描述：给定二维数组，找到其中最大的子矩阵和
 * Input: matrix = [[1,0,1],[0,-2,3]]   output： 4
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
        int currentSum = 0; int leftHalfMaxSum = inputArray[center];
        for(int i = center; i>=left; i--){
            currentSum += inputArray[i];
            if(currentSum > leftHalfMaxSum){
                leftHalfMaxSum = currentSum;
            }
        }

        //求包括右半边数组第一个元素的最大子序列和
        currentSum = 0;int rightHalfMaxSum = inputArray[center+1];
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
        if(inputArray.length==0)
            return 0;

        int maxSum = inputArray[0];int currentSum = 0;
        for(int i = 0; i < inputArray.length; i++){
            currentSum += inputArray[i];
            if (currentSum > maxSum){
                maxSum = currentSum;
            }else if (currentSum <= 0){
                currentSum = 0;
            }
        }
        return maxSum;
    }

    /**
     * 动态规划思想，本质上和上一个方法相同
     * 算法思想：
     * 设数组maxEndWithCurrentElement[],表示以当前元素结尾的最大子序列和
     * maxEndWithCurrentElement[i] = Math.max(maxEndWithCurrentElement[i-1] + currentElement,currentElement)
     * 最大子序列和就是数组maxEndWithCurrentElement[]中的最大值
     * @param inputArray
     * @return
     */
    private static int MaxSubSequenceSumDP(int[] inputArray){
        if(inputArray.length==0)
            return 0;
        int result = inputArray[0];int maxEndWithCurrentElement = 0;//以当前元素结尾的最大子序列和。
        for(int currentElement : inputArray){
            maxEndWithCurrentElement = Math.max(maxEndWithCurrentElement + currentElement,currentElement);
            result = Math.max(maxEndWithCurrentElement, result);
        }
        return result;

    }

    /**
     * 二位矩阵的最大子矩阵和问题
     *
     * 算法过程：
     * 1.以列为左右边界，遍历所有的左右边界。
     * 2.对于每一个左右边界，将同一行的数值全部相加，生成一个大小为行数的一维数组。这个数组的最大子序列和，即是在这个左右边界下最大的子矩阵和。
     * 3.遍历所有情况，可得最大的子矩阵和
     * @param matrix
     * @return
     */
    private static int MaxSubMatrixSum(int[][] matrix){
        if(matrix.length==0)
            return 0;

        int row = matrix.length;
        int col = matrix[0].length;

        int result = matrix[0][0];

        for(int i = 0; i< col; i++){
            int[] sumRow = new int[row];
            for(int j = i; j<col; j++){
                for(int h = 0; h<row; h++){
                    sumRow[h] += matrix[h][j];
                }

                result = Math.max(result,MaxSubSequenceSumDP(sumRow));
            }
        }
        return result;
    }


    public static void main(String[] args) {
        int[] inputArray = new int[100];
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(int i = 0; i<100; i++){
            inputArray[i] = random.nextInt(12);
        }
        System.out.println(MaxSubSequenceSumRec(inputArray,0, inputArray.length-1));
        System.out.println(MaxSubSequenceSumLin(inputArray));
        System.out.println(MaxSubSequenceSumDP(inputArray));
    }
}
