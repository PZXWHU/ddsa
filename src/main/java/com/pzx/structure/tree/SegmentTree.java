package com.pzx.structure.tree;

/**
 * 线段树
 * LeetCode 307. 区域和检索
 */
public class SegmentTree {

    int[] tree;
    int m;

    public SegmentTree(int[] nums) {
        this.m = nums.length;
        this.tree = new int[m * 2];
        for(int i = m; i < m * 2; i++){
            tree[i] = nums[i - m];
        }
        for(int i = m - 1; i > 0; i--){
            tree[i] = tree[i * 2] + tree[i * 2 + 1];
        }
    }

    public void update(int index, int val) {
        index += m;
        tree[index] = val;
        index /= 2;
        while(index > 0){
            tree[index] = tree[index * 2] + (index * 2 + 1 >= m * 2 ? 0 : tree[index * 2 + 1]);
            index /= 2;
        }
    }

    public int sumRange(int left, int right) {
        left += m;
        right += m;
        int sum = 0;
        while(left <= right){
            if(left % 2 == 1){
                sum += tree[left];
                left += 1;
            }
            if(right % 2 == 0){
                sum += tree[right];
                right -= 1;
            }

            left /= 2;
            right /= 2;

        }
        return sum;
    }
}
