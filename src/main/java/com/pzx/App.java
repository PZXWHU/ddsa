package com.pzx;

import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        List<Integer> lists =  Lists.newArrayList(1,2,3,4,5,6,7,7,6);
        Spliterator<Integer> spliterator = lists.spliterator();
        Spliterator<Integer> spliterator1 = spliterator.trySplit();

        spliterator.forEachRemaining(System.out::println);
        System.out.println("-------");
        spliterator1.forEachRemaining(System.out::println);


    }

    class A{
        int a;int b;int c;int d;

        public A(int a, int b, int c, int d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }

        public int getC() {
            return c;
        }

        public int getD() {
            return d;
        }
    }

}
