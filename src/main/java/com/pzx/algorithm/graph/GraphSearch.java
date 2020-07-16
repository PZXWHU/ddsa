package com.pzx.algorithm.graph;

import java.util.*;
import java.util.concurrent.Delayed;

/**
 * 图的深度优先遍历和广度优先遍历
 * 对于邻接矩阵表示的图，DFS和BFS的时间复杂度均为O（V^2），因为对于每一个遍历的点，都需要再遍历所有的点以查找其邻接点。
 * 对于邻接表表示的图，DFS和BFS的时间复杂度均为O（E + V），因为对于每一个便利的点，只需要遍历邻接表中储存的边即可获得其邻接点
 */
public class GraphSearch {

    private static final int MAX_VALUE = Integer.MAX_VALUE / 2;

    /**
     * 1、构建数据结构记录访问过的节点
     * 2、遍历图中的所有节点，对每个节点进行深度优先遍历
     * 3、每次深度优先遍历类似于多叉树的先序遍历，访问过某个节点需要进行记录
     * @param adjMatrix
     * @return
     */
    public static int[] DFS(int[][] adjMatrix){
        if (adjMatrix == null || adjMatrix.length ==0)
            return null;
        int vertexNum = adjMatrix.length;
        boolean[] isVisited = new boolean[vertexNum];
        List<Integer> dfsSort = new ArrayList<>();
        for(int i = 0; i < vertexNum; i++){
            if (!isVisited[i])
                dfs(adjMatrix,i, isVisited, dfsSort);
        }
        return dfsSort.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * 图的非递归遍历
     * 借助栈来实现。
     * 步骤1：如果栈为空，则退出程序，否则，访问栈顶节点，但不弹出栈点节点。
     * 步骤2：如果栈顶节点的所有直接邻接点都已访问过，则弹出栈顶节点，否则，将该栈顶节点的未访问的其中一个邻接点压入栈，同时，标记该邻接点为已访问，继续步骤1。
     * @param adjMatrix
     * @param vertexIndex
     * @param isVisited
     * @param dfsSort
     */
    private static void dfs(int[][] adjMatrix, int vertexIndex, boolean[] isVisited, List<Integer> dfsSort){
        Deque<Integer> stack = new LinkedList<>();
        dfsSort.add(vertexIndex);
        isVisited[vertexIndex] = true;
        stack.push(vertexIndex);

        while (!stack.isEmpty()){
            int peekVertexIndex = stack.peek();
            boolean needPop = true;
            for(int i = 0; i < adjMatrix.length; i++){
                if(adjMatrix[peekVertexIndex][i] != MAX_VALUE && !isVisited[i]){
                    dfsSort.add(i);
                    isVisited[i] = true;
                    stack.push(i);
                    needPop = false;
                    break;
                }
            }
            if (needPop)
                stack.pop();
        }
    }

    /**
     * 1、构建数据结构记录访问过的节点
     * 2、遍历图中的所有节点，对每个节点进行宽度优先遍历
     * 3、每次宽度优先遍历类似于多叉树的层级遍历，访问过某个节点需要进行记录
     * @param adjMatrix
     */
    private static int[] BFS(int[][] adjMatrix){
        if (adjMatrix == null || adjMatrix.length ==0)
            return null;
        int vertexNum = adjMatrix.length;
        boolean[] isVisited = new boolean[vertexNum];
        List<Integer> bfsSort = new ArrayList<>();
        for(int i = 0; i < vertexNum; i++){
            if (!isVisited[i])
                bfs(adjMatrix,i, isVisited, bfsSort);
        }
        return bfsSort.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * 当队列未空时，出队列，并将出队元素的所有未访问邻接节点入队列，并标记为已访问。
     * @param adjMatrix
     * @param vertexIndex
     * @param isVisited
     * @param bfsSort
     */
    private static void bfs(int[][] adjMatrix, int vertexIndex, boolean[] isVisited, List<Integer> bfsSort){
        Queue<Integer> queue = new LinkedList<>();
        bfsSort.add(vertexIndex);
        isVisited[vertexIndex] = true;
        queue.offer(vertexIndex);

        while (!queue.isEmpty()){
            int pollVertexIndex = queue.poll();
            for(int i = 0; i < adjMatrix.length; i++){
                if (adjMatrix[pollVertexIndex][i] != MAX_VALUE && !isVisited[i]){
                    bfsSort.add(i);
                    isVisited[i] = true;
                    queue.offer(i);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] matrix = new int[][]{
                {MAX_VALUE,1,MAX_VALUE,1,MAX_VALUE,1,MAX_VALUE,MAX_VALUE},
                {1,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,1,MAX_VALUE},
                {MAX_VALUE,MAX_VALUE,MAX_VALUE,1,1,MAX_VALUE,MAX_VALUE,MAX_VALUE},
                {1,MAX_VALUE,1,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE},
                {MAX_VALUE,MAX_VALUE,1,MAX_VALUE,MAX_VALUE,MAX_VALUE,1,MAX_VALUE},
                {1,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,1},
                {MAX_VALUE,1,MAX_VALUE,MAX_VALUE,1,MAX_VALUE,MAX_VALUE,1},
                {MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,MAX_VALUE,1,1,MAX_VALUE}

        };
        System.out.println(Arrays.toString(BFS(matrix)));
    }
}
