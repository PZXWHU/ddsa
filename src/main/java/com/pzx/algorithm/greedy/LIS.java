package com.pzx.algorithm.greedy;

import java.util.ArrayList;
import java.util.List;

/**
 * 最长递增子序列
 * https://leetcode-cn.com/problems/longest-increasing-subsequence/
 */
public class LIS {

    public int lengthOfLIS(int[] nums) {
        List<Integer> min = new ArrayList<>();
        int m = nums.length;

        min.add(nums[0]);

        for(int i = 1; i < m; i++){
            if(nums[i] > min.get(min.size() - 1)){
                min.add(nums[i]);
            }else if(nums[i] < min.get(min.size() - 1)){
                int left = 0;
                int right = min.size() - 1;
                int index = -1;
                while(left <= right){
                    int mid = (left + right) >> 1;
                    if(min.get(mid) < nums[i]){
                        index = mid;
                        left = mid + 1;
                    }else{
                        right = mid - 1;
                    }
                }
                min.set(index + 1, nums[i]);
            }
            // System.out.println(min + "  " + nums[i]);
        }

        return min.size();

    }

}
