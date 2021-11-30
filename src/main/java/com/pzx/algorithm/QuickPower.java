package com.pzx.algorithm;

public class QuickPower {

    public double myPow(double x, int n) {
        long b = n;
        if(n < 0){
            x = 1 / x;
            b = -b;
        }
        double res = 1;
        while( b > 0){
            if((b & 1) == 1)
                res *= x;
            x *= x;
            b >>= 1;
        }
        return res;

    }

    public static int pow(int x, int n){
        int res = 1;
        while(n > 0){
            if(n % 2 == 1)
                res = (res * x) ;
            x = (x * x) ;
            n /= 2;
        }
        return res;
    }

    public static void main(String[] args) {

    }

}
