package com.pzx.algorithm.stack;

import java.util.Deque;
import java.util.LinkedList;

/**
 * https://leetcode-cn.com/problems/132-pattern/
 */
public class Mode132 {

    public boolean find132pattern(int[] nums) {
        Deque<Integer> stack = new LinkedList<>();
        int m = nums.length;
        int max2 = Integer.MIN_VALUE;
        stack.push(nums[m - 1]);
        for(int i = m - 2; i >= 0; i--){
            if(nums[i] < max2){
                if(!stack.isEmpty()) return true;
            }else{
                while(!stack.isEmpty() && stack.peek() < nums[i]){
                    max2 = stack.pop();
                }
                stack.push(nums[i]);
            }
        }

        return false;

    }

}
