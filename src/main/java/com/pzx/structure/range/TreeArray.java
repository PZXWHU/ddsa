package com.pzx.structure.range;

/**
 * 树状数组
 * https://leetcode-cn.com/problems/count-number-of-teams/solution/tong-ji-zuo-zhan-dan-wei-shu-by-leetcode-solution/
 */
public class TreeArray {

    private int[] tree;

    public TreeArray(int[] arr){
        tree = new int[arr.length + 1];
    }

    public int lowBit(int x) {
        return x & (-x);
    }

    public void add(int index, int k){
        while (index < tree.length){
            tree[index] += k;
            index += lowBit(index);
        }
    }

    public int sum(int index){
        int sum = 0;
        while (index > 0){
            sum += tree[index];
            index -= lowBit(index);
        }

        return sum;
    }

}
