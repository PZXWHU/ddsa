package com.pzx.structure.graph;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * https://blog.csdn.net/qq_38410730/article/details/79587747?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase
 * 图的邻接表表示
 * 邻接表的存储方式是一种顺序存储与链式存储相结合的存储方式，顺序存储部分用来保存图中的顶点信息，链式存储部分用来保存图中边或弧的信息。
 * 邻接表适合储存稀疏的图
 * @param <E>
 */
public class GraphAdjList<E> implements IGraph<E> {

    private Map<E, VertexNode<E>> vertices;

    public GraphAdjList() {
        this.vertices = new HashMap<>();
    }

    @Override
    public int getNumOfVertices() {
        return vertices.size();
    }

    @Override
    public int getNumOfEdges() {
        int numOfEdges = 0;
        for (VertexNode<E> vertexNode : vertices.values()){
            AdjacentNode<E> adjacentNode = vertexNode.firstAdj;
            while (adjacentNode != null){
                numOfEdges++;
                adjacentNode = adjacentNode.nextAdj;
            }
        }
        return numOfEdges;
    }

    @Override
    public boolean insertVertex(E v) {
        VertexNode<E> insertVertexNode = new VertexNode<>(v);
        if(vertices.containsKey(v))
            return false;
        vertices.put(v, new VertexNode<>(v));
        return true;
    }

    @Override
    public boolean deleteVertex(E v) {
        if(!vertices.containsKey(v))
            return false;
        vertices.remove(v);
        for(VertexNode<E> vertexNode : vertices.values()){
            deleteAdjacentNode(vertexNode, v);
        }
        return true;
    }

    @Override
    public boolean insertEdge(E v1, E v2, int weight) {

        if(vertices.containsKey(v1) && vertices.containsKey(v2)){
            VertexNode<E> vertexNode = vertices.get(v1);
            AdjacentNode<E> adjacentNode = vertexNode.firstAdj;
            while (adjacentNode != null){
                if(adjacentNode.data.equals(v2)){
                    adjacentNode.weight = weight;
                    return true;
                }
                adjacentNode = adjacentNode.nextAdj;
            }
            AdjacentNode<E> insertAdjacentNode = new AdjacentNode<>(v2, weight);
            insertAdjacentNode.nextAdj = vertexNode.firstAdj;
            vertexNode.firstAdj = insertAdjacentNode;
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteEdge(E v1, E v2) {
        if(vertices.containsKey(v1) && vertices.containsKey(v2)){
            VertexNode<E> vertexNode = vertices.get(v1);
            return deleteAdjacentNode(vertexNode,v2);
        }
        return false;
    }

    /**
     * 删除VertexNode所连接的data等于v的邻近节点
     * @param vertexNode
     * @param v
     * @return
     */
    private boolean deleteAdjacentNode(VertexNode<E> vertexNode, E v){
        AdjacentNode<E> adjacentNode = vertexNode.firstAdj;
        if (adjacentNode.data.equals(v)){
            vertexNode.firstAdj = adjacentNode.nextAdj;
            return true;
        }
        AdjacentNode<E> pre = adjacentNode;
        adjacentNode = adjacentNode.nextAdj;
        while (adjacentNode != null){
            if (adjacentNode.data.equals(v)){
                pre.nextAdj = adjacentNode.nextAdj;
                return true;
            }
            pre = adjacentNode;
            adjacentNode = adjacentNode.nextAdj;
        }
        return false;
    }

    @Override
    public int getEdge(E v1, E v2) {
        if (vertices.containsKey(v1) && vertices.containsKey(v2)){
            VertexNode<E> vertexNode = vertices.get(v1);
            AdjacentNode<E> adjacentNode = vertexNode.firstAdj;
            while (adjacentNode != null){
                if (adjacentNode.data.equals(v2))
                    return adjacentNode.weight;
                adjacentNode = adjacentNode.nextAdj;
            }
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public int inDegree(E v) {
        if(!vertices.containsKey(v))
            return 0;
        int inDegree = 0;
        for(VertexNode<E> vertexNode : vertices.values()){
            AdjacentNode<E> adjacentNode = vertexNode.firstAdj;
            while (adjacentNode != null){
                if (adjacentNode.data.equals(v)){
                    inDegree++;
                    break;
                }
                adjacentNode = adjacentNode.nextAdj;
            }
        }
        return inDegree;
    }

    @Override
    public int outDegree(E v) {
        if(!vertices.containsKey(v))
            return 0;
        int outDegree = 0;
        VertexNode<E> vertexNode = vertices.get(v);
        AdjacentNode<E> adjacentNode = vertexNode.firstAdj;
        while (adjacentNode != null){
            outDegree++;
            adjacentNode = adjacentNode.nextAdj;
        }
        return outDegree;
    }

    @Override
    public void depthFirstSearch(E v, Consumer<E> consumer) {

    }

    @Override
    public void breadthFirstSearch(E v, Consumer<E> consumer) {

    }

    @Override
    public int[] dijkstra(E v) {
        return null;
    }

    // 邻接表中表对应的链表的顶点
    private static class AdjacentNode<E> {
        private E data;
        private int weight;// 存储边或弧相关的信息，如权值
        private AdjacentNode<E> nextAdj; // 下一个邻接表结点
        public AdjacentNode(E data, int weight) {
            this.data = data;
            this.weight = weight;
        }
    }
    // 邻接表中表的顶点
    private static class VertexNode<E> {
        private E data; // 顶点信息
        private AdjacentNode<E> firstAdj; // //邻接表的第1个结点

        public VertexNode(E data) {
            this.data = data;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(data);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if(obj == null)
                return false;
            if(!(obj instanceof VertexNode))
                return false;
            return Objects.equals(data,((VertexNode) obj).data);
        }

    };

}
