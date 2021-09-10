package com.pzx.algorithm.graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * https://leetcode-cn.com/problems/shortest-path-visiting-all-nodes/
 *
 * 这题比较巧妙的是利用mask记录了经过的所有节点，因为这题避免重复的不是已经访问的单个节点，而是已经访问的所有节点（经过的路径）
 */
public class ShortestPathLength {

    public int shortestPathLength(int[][] graph) {

        int n = graph.length;
        if(n == 0 || n == 1)  return 0 ;

        boolean[][] visited = new boolean[n][1 << n];

        Queue<int[]> queue = new LinkedList<>();
        for(int i = 0 ; i < n; i++){
            queue.offer(new int[]{i, 1 << i, 0});
            visited[i][1 << i] = true;
        }

        int min = Integer.MAX_VALUE;
        while(!queue.isEmpty()){
            int[] poll = queue.poll();
            int index = poll[0];
            int mask = poll[1];
            int dist = poll[2];

            for(int neighborIndex : graph[index]){
                if(visited[neighborIndex][mask | (1 << neighborIndex)])
                    continue;
                queue.offer(new int[]{neighborIndex, mask | (1 << neighborIndex), dist + 1});
                visited[neighborIndex][mask | (1 << neighborIndex)] = true;

                if((mask | (1 << neighborIndex)) == (1 << n) - 1){
                    min = dist + 1;
                    return min;
                }
            }
        }

        return min;


    }
}
