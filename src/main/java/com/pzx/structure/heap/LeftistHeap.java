package com.pzx.structure.heap;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 左式堆
 * 二叉树+堆序特性
 * 零路径长npl：从某一个节点到一个不具有两个儿子的节点的最短路径。
 * 具有一个孩子或者零个孩子的节点的npl为0，null的npl为-1
 * 任何一个节点的npl是它右儿子中npl的最小值加1
 *
 * 左式堆性质：堆中的每一个节点，左孩子的npl大于等于右孩子的npl
 *
 *左式堆的右子树明显小于左子树，利用右子树去进行合并，可以减少堆合并的消耗，提高合并效率
 */
public class LeftistHeap<T extends Comparable<? super T>> {

    LeftistHeapNode<T> root;


    private void merge(LeftistHeap<T> rhs){
        if(this == rhs)
            return;
        root = merge(root,rhs.root);
        rhs.root = null;

    }

    /**
     * 递归合并,时间复杂度O（logN）
     * 1.基准情况：当某一个堆为null时，返回另一个堆
     * 2.比较堆顶元素，将具有较小堆顶的堆的右孩子和另一个堆合并，结果作为具有较小堆顶的堆的右孩子
     * 3.判断第2步之后的具有较小堆顶的堆的左右孩子的npl，判断是否需要交换。并更新堆顶的npl为右孩子的npl+1
     * @param h1
     * @param h2
     * @return
     */
    private LeftistHeapNode<T> merge(LeftistHeapNode<T> h1, LeftistHeapNode<T> h2){
        if(h1 == null)
            return h2;
        if(h2 == null)
            return h1;
        //保证h1是具有较小根的堆
        if(h1.element.compareTo(h2.element) > 0){
            LeftistHeapNode<T> tmp = h1;
            h1 = h2;
            h2 = tmp;
        }
        //如果h1的left为null，那么其right肯定也为null，h1的npl不需要改变
        //其是else中的特殊情况
        if(h1.left == null)
            h1.left = h2;
        else {
            //将具有较小堆顶的右孩子和另一个堆合并
            h1.right = merge(h1.right, h2);
            //如果右孩子的npl大于左孩子，交换左右孩子
            if(h1.left.npl < h1.right.npl){
                LeftistHeapNode<T> tmp = h1.left;
                h1.left = h1.right;
                h1.right = tmp;
            }
            h1.npl = h1.right.npl + 1;
        }
        return h1;

    }

    /**
     * 时间复杂度logN
     * 插入相当于合并当前堆和一个单节点的堆
     * @param element
     */
    private void insert(T element){
        root = merge(new LeftistHeapNode<T>(element,null,null),root);
    }

    /**
     * 时间复杂度logN
     * 删除堆顶之后的堆，即合并左右子堆
     * @return
     */
    private T deleteMin(){
        if (root == null)
            return null;
        T min = root.element;
        root = merge(root.left, root.right);
        return min;
    }


    private static class LeftistHeapNode<T>{
        private T element;
        private LeftistHeapNode<T> left;
        private LeftistHeapNode<T> right;
        private int npl;

        public LeftistHeapNode(T element, LeftistHeapNode<T> left, LeftistHeapNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.npl = 0;
        }
    }

    public static void main(String[] args) {
        LeftistHeap<Integer> heap = new LeftistHeap<>();
        for(int i = 0; i< 100; i++){
            heap.insert(ThreadLocalRandom.current().nextInt(1000));
        }
        LeftistHeap<Integer> heap1 = new LeftistHeap<>();
        for(int i = 0; i< 100; i++){
            heap1.insert(ThreadLocalRandom.current().nextInt(1000));
        }
        heap.merge(heap1);
        for(int i =0; i < 200; i++){
            System.out.println(heap.deleteMin());
        }
    }

}
