package com.pzx.algorithm.backtrack;

import java.util.ArrayList;
import java.util.List;

public class NQueen {

    List<List<String>> res = new ArrayList<>();

    int n;

    public List<List<String>> solveNQueens(int n) {
        if(n == 0)
            return res;

        this.n = n;
        List<Integer> tmp = new ArrayList<>();

        dfs(0, tmp);
        return res;
    }

    void dfs(int row, List<Integer> tmp){

        if(tmp.size() == n){
            res.add(transferToList(tmp));
            return;
        }

        for(int i = 0; i < n; i++){
            if(tmp.size() > 2 && tmp.get(0) == 0 && tmp.get(1) == 3 && tmp.get(2) == 1 && row == 3 && i == 4){
                System.out.println(tmp);
                System.out.println(check(row, i,tmp));
            }
            if(check(row, i,tmp)){
                tmp.add(i);
                dfs(row + 1, tmp);
                tmp.remove(tmp.size() - 1);
            }
        }
    }

    boolean check(int row, int col, List<Integer> tmp){

        for(int i = 0; i < tmp.size(); i++){
            int row1 = i;
            int col1 = tmp.get(i);
            if(col == col1) return false;
            if(((col1 - col)  ==  (row1 - row))  || ((col1 - col)  ==  (row - row1))) return false;
        }
        return true;
    }



    List<String> transferToList(List<Integer> tmp){
        List<String> res = new ArrayList<>();
        for(int i = 0 ; i < tmp.size(); i++){
            StringBuilder sb = new StringBuilder();
            for(int j = 0 ; j < n; j++){
                if(j == tmp.get(i))
                    sb.append("Q");
                else
                    sb.append(".");
            }
            res.add(sb.toString());
        }
        return res;
    }


    public static void main(String[] args) {
        NQueen nQueen = new NQueen();
        System.out.println(nQueen.solveNQueens(5));
    }

}
