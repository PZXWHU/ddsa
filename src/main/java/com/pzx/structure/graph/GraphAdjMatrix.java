package com.pzx.structure.graph;


import javax.swing.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * https://blog.csdn.net/qq_38410730/article/details/79587747?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase
 *图的邻接矩阵表示
 *邻接矩阵方法实际上是图的一种静态存储方法,建立这种存储结构时需要预先知道图中顶点的个数(以下实现类不需要).
 * 邻接矩阵所占用的存储单元数目至于图中的顶点的个数有关，而与边或弧的数目无关，若图是一个稀疏图的话，必然造成存储空间的浪费。
 *
 */
public class GraphAdjMatrix<E> implements IGraph<E> {

    private Set<E> vertices;// 存储图的顶点的一维数组，使用Set而不是List是因为节点之间没有必要的顺序关系，而contains操作却经常使用
    private Map<E, Map<E, Integer>> edges;// 存储图的边的二维数组


    public GraphAdjMatrix() {
        this.vertices = new HashSet<>();
        this.edges = new HashMap<>();
    }

    @Override
    public int getNumOfVertices() {
        return vertices.size();
    }

    @Override
    public int getNumOfEdges() {
        int numOfEdge = 0;
        for(Map<E, Integer> vertexEdges : edges.values()){
            numOfEdge += vertexEdges.size();
        }
        return numOfEdge;
    }

    // 插入顶点

    /**
     * 省去赋值Integer.MAX_VALUE的步骤，edges中没有对应元素即视为无边
     * @param v
     * @return
     */
    @Override
    public boolean insertVertex(E v) {
        if(vertices.contains(v))
            return false;


        Map<E, Integer> insertVertexEdges = new HashMap<>();

        vertices.add(v);
        edges.put(v,insertVertexEdges);

        return true;
    }

    // 删除顶点
    @Override
    public boolean deleteVertex(E v) {
        if (!vertices.contains(v))
            return false;

        vertices.remove(v);
        edges.remove(v);
        for(Map<E, Integer> vertexEdges : edges.values()){
            vertexEdges.remove(v);
        }
        return true;
    }

    // 插入边或者更新边
    @Override
    public boolean insertEdge(E v1, E v2, int weight) {
       if(vertices.contains(v1) && vertices.contains(v2)){
           edges.get(v1).put(v2, weight);
           return true;
       }
        return false;
    }

    // 删除边
    @Override
    public boolean deleteEdge(E v1, E v2) {

        if(vertices.contains(v1) && vertices.contains(v2)){
            Integer edgeWeight = edges.get(v1).remove(v2);
            return edgeWeight == null ? false : true;
        }
        return false;

    }

    // 查找边
    @Override
    public int getEdge(E v1, E v2){

        if(vertices.contains(v1) && vertices.contains(v2)){
            Integer weight = edges.get(v1).get(v2);
            return weight == null ? Integer.MAX_VALUE : weight;
        }
        return Integer.MAX_VALUE;

    }


    @Override
    public int inDegree(E v) {
        if(!vertices.contains(v))
            return 0;
        int inDegree = 0;
        for(Map<E, Integer> vertexEdges : edges.values()){
            if (vertexEdges.containsKey(v))
                inDegree++;
        }
        return inDegree;
    }

    @Override
    public int outDegree(E v) {
        if(!vertices.contains(v))
            return 0;
        return edges.get(v).size();
    }

    /**
     * 1、构建数据结构记录访问过的节点
     * 2、遍历图中的所有节点，对每个节点进行深度优先遍历
     * 3、每次深度优先遍历类似于多叉树的先序遍历，访问过某个节点需要进行记录
     *
     * https://www.cnblogs.com/lanjianqing/p/4125134.html
     * @param v
     * @param consumer
     */
    @Override
    public void depthFirstSearch(E v, Consumer<E> consumer) {
        if (!vertices.contains(v) || consumer == null)
            return;
        Set<E> visitedVertices = new HashSet<>();

        dfsRecursively(v,visitedVertices, consumer);
        for(E vertex : vertices){
            if (!visitedVertices.contains(vertex))
                dfsRecursively(vertex,visitedVertices, consumer);
        }
    }

    private void dfsRecursively(E v, Set<E> visitedVertices, Consumer<E> consumer){
        visitedVertices.add(v);
        consumer.accept(v);
        for(E vertex : vertices){
            if(!visitedVertices.contains(vertex) && edges.get(v).containsKey(vertex))
                dfsRecursively(vertex, visitedVertices, consumer);
        }
    }

    /**
     * 图的非递归遍历
     * 借助栈来实现。
     * 步骤1：如果栈为空，则退出程序，否则，访问栈顶节点，但不弹出栈点节点。
     * 步骤2：如果栈顶节点的所有直接邻接点都已访问过，则弹出栈顶节点，否则，将该栈顶节点的未访问的其中一个邻接点压入栈，同时，标记该邻接点为已访问，继续步骤1。
     * https://blog.csdn.net/happyever2012/article/details/44755711
     * @param v
     * @param visitedVertices
     * @param consumer
     */
    private void dfsIteratively(E v, Set<E> visitedVertices, Consumer<E> consumer){
        Deque<E> stack = new LinkedList<>();
        stack.push(v);
        visitedVertices.add(v);
        consumer.accept(v);

        while (!stack.isEmpty()){
            E peekVex = stack.peek();
            boolean needPoll = true;
            for(E vertex : vertices){
                if(!visitedVertices.contains(vertex) && edges.get(peekVex).containsKey(vertex)){
                    stack.push(vertex);
                    visitedVertices.add(vertex);
                    consumer.accept(vertex);
                    needPoll = false;
                    break;
                }
            }
            if (needPoll)
                stack.pop();
        }

    }


    /**
     * 1、构建数据结构记录访问过的节点
     * 2、遍历图中的所有节点，对每个节点进行宽度优先遍历
     * 3、每次宽度优先遍历类似于多叉树的层级遍历，访问过某个节点需要进行记录
     *
     * https://www.cnblogs.com/lanjianqing/p/4125134.html
     * @param v
     * @param consumer
     */
    @Override
    public void breadthFirstSearch(E v, Consumer<E> consumer) {
        if (!vertices.contains(v) || consumer == null)
            return;
        Set<E> visitedVertices = new HashSet<>();

        bfs(v, visitedVertices, consumer);
        for(E vertex : vertices){
            if (!visitedVertices.contains(vertex)){
                bfs(vertex, visitedVertices, consumer);
            }
        }
    }

    /**
     * 当队列未空时，出队列，并将出队元素的所有未访问连接节点入队列，并标记为已访问。
     * @param v
     * @param visitedVertices
     * @param consumer
     */
    private void bfs(E v, Set<E> visitedVertices, Consumer<E> consumer){
        Queue<E> queue = new LinkedList<>();
        queue.add(v);
        visitedVertices.add(v);
        consumer.accept(v);

        while (!queue.isEmpty()){
            E pollVex = queue.poll();

            for(E vertex : vertices){
                if(!visitedVertices.contains(vertex) && edges.get(pollVex).containsKey(vertex)){
                    queue.offer(vertex);
                    visitedVertices.add(vertex);
                    consumer.accept(vertex);
                }

            }
        }

    }



    /**
     * 针对有向无圈图的拓扑排序
     * 要求排序后，对于任意有向边（v，w），v都在w之前
     * 1.将入度为0的节点入队列
     * 2.当队列非空时，出队列，并将出队元素邻近的节点的入度-1，当邻近节点入度为0时，将其入队列
     * 3.当处理的节点数小于当前图中存在的节点数时，则是有圈或是不连通（处理节点数小于当前节点数）
     * @param consumer
     */
    public void topSort(Consumer<E> consumer){
        Queue<E> queue = new LinkedList<>();
        for(E vertex : vertices){
            if(inDegree(vertex) == 0)
                queue.offer(vertex);
        }
        Map<E, Integer> inDegreeDecline = new HashMap<>();
        int count = 0;
        while (!queue.isEmpty()){
            count++;
            E pollVertex = queue.poll();
            consumer.accept(pollVertex);
            for(E adjacent : edges.get(pollVertex).keySet()){
                inDegreeDecline.merge(adjacent, 1, (x,y)->x + y);
                if (inDegree(adjacent) - inDegreeDecline.get(adjacent) == 0)
                    queue.offer(adjacent);
            }
        }
        if (count != vertices.size())
            throw new RuntimeException("当前图不是有向无圈图，不能进行拓扑排序！");

    }



    public void printAdjacentMatrix(){
        System.out.print(" ");
        for (E vertex : vertices ){
            System.out.print("     " + vertex);
        }
        System.out.println("");
        System.out.println("----------------------------------");
        for (E vertex1 : vertices ){
            System.out.print(vertex1+"|");
            for (E vertex2 : vertices ){
                Integer weight =  edges.get(vertex1).get(vertex2);
                if (weight == null)
                    System.out.print("    ∞");
                else
                    System.out.print("    "+ edges.get(vertex1).get(vertex2)+" ");
            }
            System.out.println("");
        }
        /*
        for(int i = 0; i<= curVertexIndex; i++){
            for(int j = 0; j<= curVertexIndex; j++){
                if (edges.get(i) == null)
                    continue;
                Integer weight = edges.get(i).get(j);
                if (weight == null)
                    System.out.print("   ∞");
                else
                    System.out.print("   "+ weight + " ");
            }
            System.out.println("");
        }

         */
    }

}

