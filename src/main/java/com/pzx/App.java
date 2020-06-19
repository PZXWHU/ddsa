package com.pzx;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        Integer[] ints = new Integer[10];
        Object[] objects = ints;
        objects[0] = "da";

    }
}
