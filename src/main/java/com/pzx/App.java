package com.pzx;

import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        for(String s: "x,y,z,,3,,".split(",",-1)){
            System.out.println(s);
        }

    }



}
