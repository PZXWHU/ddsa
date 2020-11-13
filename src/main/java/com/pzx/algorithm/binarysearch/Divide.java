package com.pzx.algorithm.binarysearch;

import java.util.Arrays;
import java.util.TreeMap;

public class Divide {

    public static int divide(int divisor, int dividend) {
        boolean isDiff = false;
        if((dividend ^ divisor) < 0){
            isDiff = true;
        }
        divisor = divisor > 0 ? 0 - divisor : divisor;
        dividend = dividend > 0 ? 0 - dividend : dividend;

        int res = 0;
        int sum = 0;

        //针对有溢出风险的计算，一定要使用逆向减法
        while(sum >= divisor - dividend){
            int deltaSum = dividend;
            int deltaRes = -1;

            while(divisor - deltaSum <= sum){
                sum += deltaSum;
                res += deltaRes;
                if (Integer.MIN_VALUE - deltaSum > deltaSum) break;
                deltaSum += deltaSum;
                deltaRes += deltaRes;
            }
        }


        return isDiff ? res : res == Integer.MIN_VALUE ? Integer.MAX_VALUE : 0 - res;

    }

}
