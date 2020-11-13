package com.pzx.algorithm.backtrack;

import java.util.*;

/**
 *  组合问题
 */
public class Combination {

    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> combineList = new ArrayList<>();
        if (k == 0 || n < k)
            return combineList;

        Deque<Integer> stack = new LinkedList<>();
        dfs(1, n, k, stack, combineList);
        return combineList;
    }

    private void dfs(int start, int end, int k,  Deque<Integer> stack, List<List<Integer>> combineList){

        if (stack.size() == k){
            combineList.add(new ArrayList<>(stack));
            return;
        }

        for (int i = start; i <= end - k + stack.size() + 1; i++){
            stack.push(i);
            dfs( i + 1, end, k, stack, combineList);
            stack.pop();//消除之前向下尝试的影响
        }

    }

}
