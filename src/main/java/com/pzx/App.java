package com.pzx;

import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import sun.nio.ch.DirectBuffer;
import sun.nio.ch.FileChannelImpl;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;

/**
 * Hello world!
 *
 */
public class App 
{

    public static boolean search(int[] nums, int target) {
        return search(nums, 0, nums.length - 1, target);
    }

    public static boolean search(int[] nums, int fromIndex, int toIndex, int target){
        if(fromIndex > toIndex)
            return false;

        int mid = (fromIndex + toIndex) / 2;
        if(nums[mid] == target)
            return true;
        else if(nums[mid] > target){
            if(nums[mid] >= nums[fromIndex])
                return (fromIndex <= mid - 1) && nums[Arrays.binarySearch(nums, fromIndex, mid - 1, target)] == target;
            else
                return search(nums, fromIndex, mid - 1, target);
        }else{
            if(nums[mid] <= nums[toIndex]){
                System.out.println(mid);
                System.out.println(toIndex);
                System.out.println(nums[mid] + "  " + nums[toIndex]);
                System.out.println(Arrays.binarySearch(nums, mid + 1, toIndex, target));
                return (mid + 1 <= toIndex) && nums[Arrays.binarySearch(nums, mid + 1, toIndex, target)] == target;
            }

            else
                return search(nums, mid + 1, toIndex, target);
        }
    }

    public static void main(String[] args) {
        //search(new int[]{2, 5,6,0,0,1,2}, 3);
        System.out.println(Integer.MAX_VALUE + 2);
    }

    public static String multiply(String num1, String num2) {
        if(num1 == null || num2 == null || num1.length() == 0 || num2.length() == 0)
            return "";



        char[] charArray1 = num1.toCharArray();
        char[] charArray2 = num2.toCharArray();


        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < charArray1.length; i++){
            int add = 0;
            int c1 = charArray1[i] - '0';
            for(int j = 0; j < charArray2.length; j++){
                int c2 = charArray2[j] - '0';
                if(list.size() <= j + i){
                    int multiply = c1 * c2 + add;
                    list.add(j + i, multiply % 10);
                    add = multiply / 10;
                }else{
                    int multiply = c1 * c2 + add + list.get(j + i);
                    list.set(j + i, multiply % 10);
                    add = multiply / 10;
                }

            }
            System.out.println(list);
        }
        StringBuilder sb = new StringBuilder();
        for(int i = list.size() - 1; i >= 0; i--){
            sb.append(list.get(i));
        }

        return sb.toString();


    }



    static class EventListener{
        @Subscribe
        public void listen(List<Integer> list){
            System.out.println(list.size());
        }
    }

    public static boolean isValidSudoku(char[][] board) {
        if(board == null || board.length == 0 || board[0].length ==0)
            return false;

        int[] row = new int[9];
        int[] col = new int[9];
        int[] grid = new int[9];

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(board[i][j] == ',')
                    continue;
                int num = board[i][j] - '0';


                if((row[i] >> (num - 1) & 1) == 1){
                    System.out.println("i : "+i + "  j : " + j + "  num : " + num);
                    return false;
                }

                row[i] = row[i] | (1 << (num - 1));

                /*
                if((col[j] >> (num - 1) & 1) == 1)
                    return false;
                col[j] = col[j] | (1 << (num - 1));

                int gridIndex = 3 * (i / 3) + j / 3;
                if((grid[gridIndex] >> (num - 1) & 1) == 1)
                    return false;
                grid[gridIndex] = grid[gridIndex] | (1 << (num - 1));
                */

            }

        }
        return true;
    }

}
