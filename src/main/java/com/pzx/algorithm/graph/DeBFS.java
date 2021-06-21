package com.pzx.algorithm.graph;

import java.util.*;

/**
 * 双向BFS
 * 适用场景：已知搜索的起点和终点
 * 原理：
 * 1. 设置两个遍历队列和两个记录访问节点集合
 * 2. 将起始点加入队列和集合中
 * 3. 如果两个队列均不为空：
 *      对size较小的队列进行处理：
 *          获取当前队列size；
 *          循环将队列中size个元素出队（即一次性处理某一层的所有节点），并进行以下处理：
 *              对当前节点进行bfs处理，即获取当前节点的邻接节点：
 *                  如果邻接节点被当前队列对应的访问节点集合中，则忽略
 *                  如果邻接节点被另一个队列对应的访问节点集合中，则双向bfs搜索相遇。视情况进行处理，有可能立刻返回结果，也有可能需要遍历完这一层的所有节点，才能确定最终节点
 *                  否则，将邻接节点入队，并且加入当前队列对应的访问节点集合
 *
 * 为什么要一次性处理某一层的所有节点？
 * http://www.cppblog.com/Yuan/archive/2011/02/23/140553.html
 *
 *
 *
 * 示例代码选自：LC 399. 除法求值
 * https://leetcode-cn.com/problems/evaluate-division/
 */
public class DeBFS {

    Map<String, List<Pair>> graph = new HashMap<>();

    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        createGraph(equations, values);
        double[] res = new double[queries.size()];
        for(int i = 0 ; i < queries.size(); i++){
            res[i] = queryGraph(queries.get(i));
        }

        return res;
    }

    private void createGraph(List<List<String>> equations, double[] values){
        for(int i = 0 ; i < equations.size(); i++){
            List<String> equation = equations.get(i);
            double answer = values[i];

            if(!graph.containsKey(equation.get(0)))
                graph.put(equation.get(0), new ArrayList<>());
            graph.get(equation.get(0)).add(new Pair(equation.get(1), answer));

            if(!graph.containsKey(equation.get(1)))
                graph.put(equation.get(1), new ArrayList<>());
            graph.get(equation.get(1)).add(new Pair(equation.get(0), 1 / answer));

        }
    }

    private double queryGraph(List<String> query){
        String s1 = query.get(0);
        String s2 = query.get(1);
        if(!graph.containsKey(s1) || !graph.containsKey(s2))
            return -1;
        if(s1.equals(s2))
            return 1;

        Deque<Pair> queue1 = new LinkedList<>();
        Map<String, Double> visited1 = new HashMap<>();
        queue1.offer(new Pair(s1, 1.0));
        visited1.put(s1, 1.0);

        Deque<Pair> queue2 = new LinkedList<>();
        Map<String, Double> visited2 = new HashMap<>();
        queue2.offer(new Pair(s2, 1.0));
        visited2.put(s2, 1.0);

        while(!queue1.isEmpty() && !queue2.isEmpty()){
            Double res = null;
            if(queue1.size() <= queue2.size())
                res = bfs(queue1, visited1, queue2, visited2, false);
            else
                res = bfs(queue2, visited2, queue1, visited1, true);

            if(res != null) return res;
        }

        return -1;

    }

    private Double bfs(Deque<Pair> queue1, Map<String, Double> visited1,
                       Deque<Pair> queue2, Map<String, Double> visited2, boolean reverse){
        int size = queue1.size();
        while(size-- > 0){
            Pair pollPair = queue1.poll();

            for(Pair pair : graph.get(pollPair.string)){
                if(visited1.containsKey(pair.string))
                    continue;
                double tmp = reverse ?  pollPair.number / pair.number : pollPair.number * pair.number;
                if(visited2.containsKey(pair.string)){

                    return visited2.get(pair.string) * tmp;
                }

                queue1.offer(new Pair(pair.string, tmp));
                visited1.put(pair.string, tmp);

            }
        }
        return null;
    }


    class Pair{
        String string;
        double number;
        public Pair(String string, double number){
            this.string = string;
            this.number = number;
        }
    }

}
