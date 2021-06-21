package com.pzx.algorithm.subArr;
import java.util.*;
/**
 * lc5675. 最接近目标值的子序列和
 * https://leetcode-cn.com/problems/closest-subsequence-sum/
 *
 *
 * 值得借鉴的是求所有子数组和的方法：dp动态规划
 *
 */
public class MinAbsDifference {

    public int minAbsDifference(int[] nums, int goal) {
        int m = nums.length;
        int l = m / 2;
        int r = m - l;
        //求左半数组的所有子数组和
        int[] leftSubArrSum = new int[1 << l];
        for(int i = 0 ; i < (1 << l); i++){
            for(int j = 0; j < l; j++){
                if((i & (1 << j)) == 0) continue;
                leftSubArrSum[i] = leftSubArrSum[i - (1 << j)] + nums[j];
                break;
            }
        }
        //求右半数组的所有子数组和
        int[] rightSubArrSum = new int[1 << r];
        for(int i = 0; i < (1 << r); i++){
            for(int j = 0; j < r; j++){
                if((i & (1 << j)) == 0) continue;
                rightSubArrSum[i] = rightSubArrSum[i - (1 << j)] + nums[j + l];
                break;
            }
        }

        int min = Integer.MAX_VALUE;

        Arrays.sort(leftSubArrSum);
        min = Math.min(min, minAbsSubArrDifference(leftSubArrSum, goal));
        if(min == 0) return min;
        Arrays.sort(rightSubArrSum);
        min = Math.min(min, minAbsSubArrDifference(rightSubArrSum, goal));
        if(min == 0) return min;

        int i = 0; int j = rightSubArrSum.length - 1;
        while(i < leftSubArrSum.length && j >= 0){
            int sum = leftSubArrSum[i] + rightSubArrSum[j];
            min = Math.min(min, Math.abs(sum - goal));
            if(sum == goal)
                break;
            else if(sum > goal)
                j--;
            else
                i++;
        }
        return min;
    }

    private int minAbsSubArrDifference(int[] nums, int goal){
        int min = Integer.MAX_VALUE;
        int index = Arrays.binarySearch(nums, goal);
        if(index >= 0) return 0;
        index = -1 - index;
        if(index - 1 > 0) min = Math.min(min, Math.abs(nums[index - 1] - goal));
        if(index <  nums.length) min = Math.min(min, Math.abs(nums[index] - goal));
        return min;
    }


}
