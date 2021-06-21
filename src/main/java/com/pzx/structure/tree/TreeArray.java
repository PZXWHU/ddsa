package com.pzx.structure.tree;

/**
 * 树状数组：动态维护前缀和
 */
public class TreeArray {

    int[] tree;
    int m;

    public TreeArray(int[] nums){
        this.m = nums.length;
        tree = new int[m + 1];
        for(int i = 0; i < m ; i++){
            this.update(i, nums[i]);
        }
    }

    //取x的二进制下最右1个1与之后的0组成的数大小
    private int lowBit(int k){
        return k & (-k);
    }

    public void update(int index, int delta) {
        index += 1;
        while (index <= m){
            tree[index] += delta;
            index += lowBit(index);
        }
    }

    public int query(int index){
        index += 1;
        int count = 0;
        while (index > 0){
            count += tree[index];
            index -= lowBit(index);
        }
        return count;
    }



}
