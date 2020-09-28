package com.pzx.algorithm.disjoint;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个未排序的整数数组，找出最长连续序列的长度。
 *
 * 要求算法的时间复杂度为 O(n)。
 *
 * 示例:
 *
 * 输入: [100, 4, 200, 1, 3, 2]
 * 输出: 4
 * 解释: 最长连续序列是 [1, 2, 3, 4]。它的长度为 4。
 */
public class LongestConsecutive {

    /**
     * 用Map隐式的构造并查集。键值连续的key属于一个等价集。
     * 并查集数据结构要求属于一个等价集的两个元素find返回的结果相同
     *
     * find函数返回包含key为x的等价集中最大的key值
     *
     * @param x
     * @param map
     * @return
     */
    public int find(int x, Map<Integer,Integer> map) {
        return map.containsKey(x)?find(x+1, map):x;
    }
    public int longestConsecutive(int[] nums) {
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        int max=0;
        for (int i : nums) {
            if (!map.containsKey(i - 1)) {
                max=Math.max(max,find(i+1 , map)-i);
            }
        }
        return max;
    }

}
