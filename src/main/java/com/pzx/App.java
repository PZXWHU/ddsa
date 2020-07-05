package com.pzx;

import java.util.Arrays;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       f(new int[]{1,2,3});

    }

    public static void f(int... a){
        System.out.println(Arrays.toString(a));
    }

}
