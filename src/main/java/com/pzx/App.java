package com.pzx;

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
        System.out.println( 3^3^3);

    }

    public  static class A{
        public void f(){
            System.out.println("A.f");
        }
    }

    public static class B extends A implements Test1,Test2{

    }

    public static interface Test1 {
        default void f() throws  BException {

        };
    }

    public static interface Test2  {
        default void f() throws AException{

        };

    }

    public static class AException  extends Exception{

    }
    public static class BException  extends Exception{

    }

}
