package com.pzx.algorithm.graph;

import java.util.*;

/**
 * 拓扑排序,针对有向无圈图
 *
 * 针对有向无圈图的拓扑排序
 * 要求排序后，对于任意有向边（v，w），v都在w之前
 * 1.将入度为0的节点入队列
 * 2.当队列非空时，出队列，并将出队元素邻近的节点的入度-1，当邻近节点入度为0时，将其入队列
 * 3.当处理的节点数小于当前图中存在的节点数时，则是有圈或是不连通（处理节点数小于当前节点数）
 */
public class TopologySort {

    private static final int MAX_VALUE = Integer.MAX_VALUE / 2;

    private static int[] topSort(int[][] adjMatrix){
        if (adjMatrix == null || adjMatrix.length ==0)
            return null;

        int vertexNum = adjMatrix.length;
        int[] inDegree = new int[vertexNum];
        Queue<Integer> queue = new LinkedList<>();

        for(int i = 0 ; i < vertexNum; i++){
            for (int j = 0; j < vertexNum; j++){
                if (adjMatrix[i][j] != MAX_VALUE){
                    inDegree[j]++;
                }
            }
        }
        //将入度为0的节点入队列
        for (int i = 0; i < vertexNum; i++){
            if (inDegree[i] == 0){
                queue.offer(i);
            }
        }

        int[] topSort = new int[vertexNum];
        int count = 0;
        while (!queue.isEmpty()){
            count++;
            int vertexIndex = queue.poll();
            topSort[count - 1] = vertexIndex;
            for (int i = 0; i < vertexNum; i++){
                if (adjMatrix[vertexIndex][i] != MAX_VALUE){
                    if(--inDegree[i] == 0){
                        queue.offer(i);
                    }
                }
            }
        }
        if(count != vertexNum)
            throw new RuntimeException("此图存在圈或者不连通，无法进行拓扑排序！");
        return topSort;
    }

    public static void main(String[] args) {
        int[][] matrix = new int[][]{
                {MAX_VALUE,1,1,1,MAX_VALUE,MAX_VALUE,MAX_VALUE},
                {MAX_VALUE,MAX_VALUE,MAX_VALUE,1,1,MAX_VALUE,MAX_VALUE},
                {MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,1,MAX_VALUE},
                {MAX_VALUE,MAX_VALUE,1,MAX_VALUE,MAX_VALUE,1,1},
                {MAX_VALUE,MAX_VALUE,MAX_VALUE,1,MAX_VALUE,MAX_VALUE,1},
                {MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE},
                {MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,1,MAX_VALUE}
        };
        System.out.println(Arrays.toString(topSort(matrix)));
    }

}
