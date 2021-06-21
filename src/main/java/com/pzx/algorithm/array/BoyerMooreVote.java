package com.pzx.algorithm.array;

import java.util.Arrays;

/**
 * 摩尔投票
 * 找出数组（长度N）中出现大多次数超过N/2的数
 */
public class BoyerMooreVote {

    /**
     * 假设数组是非空的，并且给定的数组总是存在多数元素
     * @param nums
     * @return
     */
    public int majorityElement(int[] nums) {
        int candidate = nums[0];
        int count = 1;
        for(int i = 1; i < nums.length; i++){
            count += nums[i] == candidate ? 1 : -1;
            if (count == 0){
                candidate = nums[++i];
                count = 1;
            }
        }

        return  candidate;
    }


}
