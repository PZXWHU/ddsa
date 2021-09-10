package com.pzx.algorithm.binarysearch;

/**
 * https://leetcode-cn.com/problems/arranging-coins/
 */
public class ArrangeCoins {

    public int arrangeCoins(int n) {
        int left = 0;
        int right = n;
        int res = 0;
        while(left <= right){
            int mid = (left + right) >> 1;
            if(sum((long)mid) == n) return mid;
            else if(sum(mid) < n){
                res = mid;//利用一个变量记录合适的答案，就不用在结束循环的时候再去麻烦的判断对应的位置
                left = mid + 1;
            }else
                right = mid - 1;
        }

        return res;

    }

    public long sum(long x){
        return x * (x + 1) / 2;
    }

}
