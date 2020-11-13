package com.pzx;

import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )throws Exception {


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
