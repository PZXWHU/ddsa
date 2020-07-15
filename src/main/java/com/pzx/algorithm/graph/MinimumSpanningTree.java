package com.pzx.algorithm.graph;

import com.pzx.algorithm.MaxSubSequenceSum;
import com.pzx.structure.disjoint.DisjointSet;

import java.util.*;

/**
 * 最小生成树
 */
public class MinimumSpanningTree {

    private static final int MAX_VALUE = Integer.MAX_VALUE / 2;

    /**
     * 贪婪算法：每次从未确定点中选择与已确定点具有最小边权值的点
     *
     * Prim算法：和dijkstra算法很像，但是更新法则不同
     * 1、创建三个数组，distance、isVisited和path数组，分别储存当前点到已访问点中的任意点的最小距离和点是否已经访问，以及使当前位置节点更新的最后的邻近节点
     * 2、将distance设置为最大值，起始点位置设置为0
     * 3、从distance中选择未访问过且具有最小值的节点，将其标识为已访问，且更新其所邻接的所有点，如果邻接节点更新了，还需要将path中邻接节点对应位置赋值为当前访问节点
     * （更新规则为当前值和当前访问点与邻接点的边的权值两者之间的最小值）
     * 4、重复步骤3，直到所有点已访问
     * 5、Path数组中储存的边即是最小生成数中存在的边
     *
     * 算法理解：
     * distance实际上储存的是，未访问点到任意已访问点的最小距离。
     * 每次选取都会选择，距离当前已访问点中任意点最近的节点(权值最小的边)，并更新其邻近点的distance
     * @param adjMatrix
     * @param s
     * @return
     */
    private static int[][] prim(int[][] adjMatrix, int s){
        if (adjMatrix == null || adjMatrix.length == 0)
            return null;

        int vertexNum = adjMatrix.length;
        int[] distance = new int[vertexNum];
        boolean[] isVisited = new boolean[vertexNum];
        int[] path = new int[vertexNum];

        Arrays.fill(distance,MAX_VALUE);
        Arrays.fill(isVisited, false);
        Arrays.fill(path, s);

        distance[s] = 0;
        for(int i = 0; i < vertexNum; i++){
            int minDistance = MAX_VALUE;
            int minIndex = -1;
            for(int j = 0;j < vertexNum; j++){
                if(!isVisited[j] && distance[j] < minDistance){
                    minDistance = distance[j];
                    minIndex = j;
                }
            }
            if (minIndex == -1)
                break;
            isVisited[minIndex] = true;
            for(int j = 0;j < vertexNum; j++){
                if (adjMatrix[minIndex][j] != MAX_VALUE && !isVisited[j] && distance[j] > adjMatrix[minIndex][j]){
                    distance[j] = adjMatrix[minIndex][j];
                    path[j] = minIndex;
                }
            }
        }

        int[][] minimumSpanningTreeEdge = new int[vertexNum][2];
        for(int i = 0; i < vertexNum; i++){
            if (i == s)
                continue;
            minimumSpanningTreeEdge[i] = new int[]{i, path[i]};
        }
        return minimumSpanningTreeEdge;
    }

    /**
     * 也是一种贪婪算法，每次选取权值最小的边，如果新加入的边与已存在的边形成圈，则舍去，选择下一个。
     * 为了简单实现不成环的检测，使用并查集。
     * 一开始所有的节点属于独立的集合，当添加一条边时，会将其所连接的节点进行union。
     * 如果新加入的边所连接的节点在一个union，那么新加入的边一定会形成圈，则舍去
     * @param adjMatrix
     * @return
     */
    private static int[][] kruskal(int[][] adjMatrix){
        if (adjMatrix == null || adjMatrix.length == 0)
            return null;

        int vertexNum = adjMatrix.length;
        DisjointSet disjointSet = new DisjointSet(vertexNum);
        Queue<int[]> queue = new PriorityQueue<>((x, y) -> Integer.compare(x[0], y[0]));

        for(int i = 0; i < vertexNum; i++){
            //这里是因为无向图的邻接矩阵会有两条边，只让一条进行队列
            for (int j = 0; j <= i; j++){
                if (adjMatrix[i][j] != MAX_VALUE){
                    queue.offer(new int[]{adjMatrix[i][j], i, j});
                }
            }
        }

        List<int[]> minimumSpanningTreeEdge = new ArrayList<>();
        while (!queue.isEmpty()){
            int[] poll = queue.poll();
            if (disjointSet.find(poll[1]) == disjointSet.find(poll[2]))
                continue;
            disjointSet.union(poll[1],poll[2]);
            minimumSpanningTreeEdge.add(new int[]{poll[1],poll[2]});
        }
        return minimumSpanningTreeEdge.toArray(new int[1][1]);

    }





    public static void main(String[] args) {
        int[][] matrix = new int[][]{
                {MAX_VALUE,2,4,1,MAX_VALUE,MAX_VALUE,MAX_VALUE},
                {2, MAX_VALUE, MAX_VALUE, 3, 10, MAX_VALUE, MAX_VALUE},
                {4,MAX_VALUE,MAX_VALUE,2,MAX_VALUE,5,MAX_VALUE},
                {1,3,2,MAX_VALUE,7,8,4},
                {MAX_VALUE,10,MAX_VALUE,7,MAX_VALUE,MAX_VALUE,6},
                {MAX_VALUE,MAX_VALUE,5,8,MAX_VALUE,MAX_VALUE,1},
                {MAX_VALUE,MAX_VALUE,MAX_VALUE,4,6,1,MAX_VALUE}
        };
        for(int[] ints: kruskal(matrix)){
            System.out.println(Arrays.toString(ints));
        }
    }

}
