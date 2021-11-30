package com.pzx.algorithm;

public class Gcd {

    public int gcd(int x, int y){
        if(y == 0)
            return x;
        return gcd(y, x % y);

    }


}
