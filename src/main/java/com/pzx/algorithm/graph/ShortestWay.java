package com.pzx.algorithm.graph;

import sun.security.provider.certpath.Vertex;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 最短路径
 */
public class ShortestWay {

    private static final int MAX_VALUE = Integer.MAX_VALUE / 2;

    /**
     * dijkstra算法（权必须为正值）：时间复杂度O（E + V^2）(每条边最多一次更新E，每次查找最小值V^2)
     * 1、创建两个数组distance和isVisited，分别记录最短距离估计值和节点是否访问过
     * 2、先将distance初始化为最大值，将起始点位置赋值为0；
     * 3、查找distance中未被访问过的节点位置的最小值，将其确定为此位置节点到起始点的最短距离,将该节点标记为已访问，并更新所有与之邻接的节点的最短距离估计值
     * （min（邻接节点现有的最短距离估计值， 邻接节点到当前节点的路径权值+当前节点的最短路径））。
     * 4、重复第三步，直到所有节点均被访问过。
     *
     * 算法理解：
     * 本质上将所有节点分为两部分，一部分为访问过且确定最短距离的节点（放入黑箱），一部分是还未确定最短距离的节点。
     * 最短距离估计值的意义是，起始点通过黑箱连接到某点的最小距离（中间可以经过黑箱的任意个点）。
     * 因为假设所有的权值为正，对于还未确定的点中的最小的最短距离估计值的点，不可能通过黑箱之外的任何点到达此点可获得更短距离，所以此点的最短距离估计值就是最短距离。
     * @param adjMatrix 图的邻接矩阵
     * @param s 起始点
     * @return
     */
    private static int[] dijkstra(int[][] adjMatrix, int s){
        if(adjMatrix == null || adjMatrix.length == 0)
            return null;
        int vertexNum = adjMatrix.length;
        int[] distance = new int[vertexNum];
        boolean[] isVisited = new boolean[vertexNum];

        Arrays.fill(distance, MAX_VALUE);
        Arrays.fill(isVisited, false);
        distance[s] = 0;

        for(int i = 0; i < vertexNum; i++){
            int minDistance = MAX_VALUE;
            int minIndex = -1;
            for(int j = 0; j< vertexNum; j++){
                if(!isVisited[j] && distance[j] < minDistance){
                    minDistance = distance[j];
                    minIndex = j;
                }
            }
            if (minIndex == -1)
                break;
            isVisited[minIndex] = true;
            for(int j = 0; j < vertexNum; j++){
                if(adjMatrix[minIndex][j] != MAX_VALUE && !isVisited[j] && distance[j] > minDistance + adjMatrix[minIndex][j]){
                    distance[j] = minDistance + adjMatrix[minIndex][j];
                }
            }
        }

        return distance;

    }

    /**
     * 上述算法在查找最小的最短距离估计值时，扫描所有节点，所以会有O（N^2）的时间复杂度，可以使用优先队列进行优化
     * 时间复杂度O（E * logV + V * logV），每次更新需要插入堆 E * logV，查找最小值即堆deleteMin  V * logV，因为有重复出队情况，所以E * logV
     * 所以最终时间复杂度O（E * logV）
     *
     * 对于无圈图，还可以用拓扑排序的方式来选择最小的最短估计距离，因为根据拓扑排序的定义，一个节点没有入度时被选取，没有入度，则其最短估计距离也就不会再被改变。
     * 利用拓扑排序的时间复杂度为O（E + V）
     * @param adjMatrix
     * @param s
     * @return
     */
    private static int[] dijkstra2(int[][] adjMatrix, int s){
        if(adjMatrix == null || adjMatrix.length == 0)
            return null;
        int vertexNum = adjMatrix.length;
        int[] distance = new int[vertexNum];
        boolean[] isVisited = new boolean[vertexNum];

        Arrays.fill(distance, MAX_VALUE);
        Arrays.fill(isVisited, false);
        distance[s] = 0;

        Queue<int[]> queue = new PriorityQueue<>((int[] x,int[] y)->Integer.compare(x[0],y[0]));
        queue.offer(new int[]{distance[s],s});

        while (!queue.isEmpty()){
            int[] poll = queue.poll();

            //因为对于一个节点可能因为修改最短估计距离多次入队列，所以出队列时需要进行判断
            if(isVisited[poll[1]])
                continue;

            int minDistance = poll[0];
            int index = poll[1];
            isVisited[index] = true;

            for(int j = 0; j < vertexNum; j++){
                if(adjMatrix[index][j] != MAX_VALUE && !isVisited[j] && distance[j] > minDistance + adjMatrix[index][j]){
                    distance[j] = minDistance + adjMatrix[index][j];
                    queue.offer(new int[]{distance[j], j});

                }
            }
        }
        return distance;
    }

    /**
     * 弗洛伊德算法，求出每个点到每个点的最小距离   时间复杂度O（V^3）
     * 弗洛伊德算法选取某个节点k作为i到j需要经过的中间节点，通过比较d(i,k)+d(k,j)和现有d(i,j)的大小，
     * 将较小值更新为路径长度，对k节点的选取进行遍历，以得到在经过所有节点时i到j的最短路径长度。
     *
     * 假设最短路径 1-3-8-4-6-9-2-5，对于路径上的任意两点，之间的路径也是这两点的最短路径。
     * 所以此最短路径可以分成：1-3-8-4-6-9和9-2-5，同理：（每次按照中间最大节点分开）
     * 分成：1-3-8、8-4-6、6-9、9-2-5
     * 当将1、2、3、4、5、6、7、8、9依次作为中间节点时：
     * 2：（9，5） = （9，2）+（2，5）被更新为最短距离
     * 3：（1，8） = （1，3）+（3，8）被更新为最短距离
     * 4：（8，6） = （8，4）+ （4，6）被更新为最短距离
     * 6：（8，9） = （8，6）+（6，9）被更新为最短距离
     * 8：（1，9） = （1，8）+（8，9）被更新为最短距离
     * 9：（1，5） = （1，9）+ （9，5）被更新为最短距离
     * 所有的最短路径都可以在k次更新中获得最小距离
     * @param adjMatrix
     * @return
     */
    private static int[][] floyd(int[][] adjMatrix){
        if (adjMatrix == null || adjMatrix.length == 0)
            return null;
        int vertexNum = adjMatrix.length;
        int[][] distance = new int[vertexNum][vertexNum];
        for(int i = 0; i < vertexNum; i++){
            for(int j = 0; j < vertexNum; j++){
                distance[i][j] = adjMatrix[i][j];
            }
        }
        for (int k = 0; k < vertexNum; k++){
            for (int i = 0; i < vertexNum; i++){
                for(int j = 0; j < vertexNum; j++){
                    if(distance[i][k] + distance[k][j] < distance[i][j]){
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }
        return distance;
    }


    /**
     * 时间复杂度O（E * V）
     * 单源的最短路径算法，可处理负权值，但是图中不能有负值圈。
     * 其做法根floyd的做法很像，但是因为是单源的最短路径，所以没必要把所有的点都当作中间点，也没必要更新所有的路径
     * 每次只会将跟起始点连接或者间接连接的节点进行入队（出队时当作中间点），以及进行最短估计距离的更新
     *
     * @param adjMatrix
     * @param s
     * @return
     */
    private static int[] weightedNegative(int[][] adjMatrix, int s){
        if(adjMatrix == null || adjMatrix.length == 0)
            return null;
        int vertexNum = adjMatrix.length;
        int[] distance = new int[vertexNum];
        Arrays.fill(distance, MAX_VALUE);

        Queue<Integer> queue = new LinkedList<>();
        distance[s] = 0;
        queue.offer(s);

        while (!queue.isEmpty()){
            int index = queue.poll();
            for(int i = 0; i < vertexNum; i++){
                if(adjMatrix[index][i] != MAX_VALUE && distance[index] + adjMatrix[index][i] < distance[i]){
                    distance[i] = distance[index] + adjMatrix[index][i];
                    if(!queue.contains(i))//这是一个不好的做法
                        queue.offer(i);
                }
            }
        }
        return distance;

    }




    public static void main(String[] args) {

        int MAX_WEIGHT = MAX_VALUE;
        // 初始化图
        int[] graph0 = new int[]{0, 1, 5, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT};
        int[] graph1 = new int[]{1, 0, 3, 7, 5, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT};
        int[] graph2 = new int[]{5, 3, 0, MAX_WEIGHT, 1, 7, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT};
        int[] graph3 = new int[]{MAX_WEIGHT, 7, MAX_WEIGHT, 0, 2, MAX_WEIGHT, 3, MAX_WEIGHT, MAX_WEIGHT};
        int[] graph4 = new int[]{MAX_WEIGHT, 5, 1, 2, 0, 3, 6, 9, MAX_WEIGHT};
        int[] graph5 = new int[]{MAX_WEIGHT, MAX_WEIGHT, 7, MAX_WEIGHT, 3, 0, MAX_WEIGHT, 5, MAX_WEIGHT};
        int[] graph6 = new int[]{MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 3, 6, MAX_WEIGHT, 0, 2, 7};
        int[] graph7 = new int[]{MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 9, 5, 2, 0, 4};
        int[] graph8 = new int[]{MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 5, 4, 0};

        int[][] matrix = new int[][]{graph0,graph1,graph2,graph3,graph4,graph5,graph6,graph7,graph8};
        /**
        for(int[] ints : floyd(matrix)){
            System.out.println(Arrays.toString(ints));
        }
         */
        System.out.println(Arrays.toString(dijkstra2(matrix, 0)));


    }


}
