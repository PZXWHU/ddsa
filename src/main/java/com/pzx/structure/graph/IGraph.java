package com.pzx.structure.graph;


import java.util.Vector;
import java.util.function.Consumer;

public interface IGraph<E> {

    int getNumOfVertices();//获取顶点的个数

    int getNumOfEdges();//获取有向边的个数

    boolean insertVertex(E v);//插入顶点

    boolean deleteVertex(E v);//删除顶点

    boolean insertEdge(E v1, E v2,int weight);//插入或者更新边

    default boolean insertDoubleEdge(E v1, E v2,int weight){
        return insertEdge(v1, v2, weight) && insertEdge(v2, v1, weight);
    }

    boolean deleteEdge(E v1, E v2);//删除边

    int getEdge(E v1,E v2);//查找边

    //从图中查找一条边(v1,v2)是否存在
    default boolean isEdgeExist(E v1,E v2){
        return getEdge(v1, v2) == Integer.MAX_VALUE ? false : true;
    }

    int inDegree(E v); //返回顶点i的入度

    int outDegree(E v); //返回顶点i的出度

    void depthFirstSearch(E v, Consumer<E> consumer);//深度优先搜索遍历

    void breadthFirstSearch(E v, Consumer<E> consumer);//广度优先搜索遍历

}

