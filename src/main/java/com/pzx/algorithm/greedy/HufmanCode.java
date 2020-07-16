package com.pzx.algorithm.greedy;

import com.pzx.structure.heap.BinaryHeap;

import java.util.*;

/**
 * 哈夫曼编码
 * 构造一棵总价值最小的满二叉树（所有的节点要么是树叶，要么有两个儿子），其中所有的字符全都在树叶上
 * 得到的字符编码也称前缀编码，因为所有字符都在树叶上，所以没有某个字符编码是另一个字符编码的前缀。所以哈夫曼编码可以直接解译。
 */
public class HufmanCode<T> {


    private static <T> Map<T, String> hufmanCode(Map<T, Integer> dataSetFrequency){
        Queue<BinaryTreeNode<T>> queue = new PriorityQueue<>((node1, node2)->Integer.compare(node1.frequency, node2.frequency));
        for(Map.Entry<T, Integer> entry : dataSetFrequency.entrySet()){
            queue.offer(new BinaryTreeNode<T>(entry.getKey(), entry.getValue(), null, null));
        }

        while (queue.size() >= 2){
            BinaryTreeNode<T> node1 = queue.poll();
            BinaryTreeNode<T> node2 = queue.poll();
            queue.offer(new BinaryTreeNode<T>(null, node1.frequency + node2.frequency, node1, node2));
        }

        BinaryTreeNode<T> root = queue.poll();
        Map<T, String> dataSetCode = new HashMap<>();
        createDataCode(dataSetCode,root, "");

        return dataSetCode;
    }

    /**
     * 递归求编码
     * @param dataSetCode
     * @param node
     * @param code
     * @param <T>
     */
    public static <T> void createDataCode(Map<T, String> dataSetCode, BinaryTreeNode<T> node, String code){
        if(node.left == null && node.right == null)
            dataSetCode.put(node.data, code);
        else {
            createDataCode(dataSetCode, node.left, code + "0");
            createDataCode(dataSetCode, node.right, code + "1");
        }
    }



    private static class BinaryTreeNode<T>{
        private T data;
        private int frequency;
        private BinaryTreeNode<T> left;
        private BinaryTreeNode<T> right;

        public BinaryTreeNode(T data, int frequency, BinaryTreeNode<T> left, BinaryTreeNode<T> right) {
            this.data = data;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "BinaryTreeNode{" +
                    "data=" + data +
                    ", frequency=" + frequency +
                    '}';
        }
    }

    public static void main(String[] args) {
        Map<String, Integer> characterSetFrequency = new HashMap<>();
        characterSetFrequency.put("a",10);
        characterSetFrequency.put("e",15);
        characterSetFrequency.put("i",12);
        characterSetFrequency.put("s",3);
        characterSetFrequency.put("t",4);
        characterSetFrequency.put("sp",13);
        characterSetFrequency.put("nl",1);
        for (Map.Entry<String, String> entry : hufmanCode(characterSetFrequency).entrySet()){
            System.out.println(entry.getKey() + ":" + entry.getValue() );
        }
    }
}
