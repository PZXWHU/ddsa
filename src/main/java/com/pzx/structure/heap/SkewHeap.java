package com.pzx.structure.heap;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 斜堆
 * 斜堆(Skew Heap)基于左倾堆的概念，也是一个用于快速合并的堆结构，但它可自我调整(self-adjusting)，
 * 每一个merge操作的平摊成本仍为O(logN)，其中N为结点数，而且和左倾堆相比，每个结点没有npl属性，从而节省了空间。
 *
 * 斜堆并不能保证左倾，但是每一个合并操作（也是采取右路合并）同时需要无条件交换，从而可以达到平摊意义上的左倾效果。注意：一颗子树r和NULL子树合并时并不需要交换r子树的左右子树。
 * 由于斜堆并不是严格的左倾堆，最坏的情况下右路长度可能为N，因此采用递归调用merge的风险是出现stack overflow。
 *
 * 所有操作的最坏运行时间为O（N），对任意次操作，平均时间复杂度为O（logN）
 */
public class SkewHeap<T extends Comparable<? super T>> {

    private SkewHeapNode<T> root;

    public SkewHeap(){

    }

    public void insert(T element){
        SkewHeap<T> heap = new SkewHeap<>();
        heap.root = new SkewHeapNode<>(element,null,null);
        merge(heap);
    }

    public T deleteMin(){
        if (root == null)
            return null;
        T min = root.element;
        root = iterativeMerge(root.left,root.right);
        return min;
    }

    /**
     * 时间复杂度logN
     * @param heap
     */
    private void merge(SkewHeap<T> heap){
        if(this == heap)
            return;
        root = iterativeMerge(root,heap.root);
        heap.root = null;
    }

    /**
     * 合并操作和左式堆很相似，采用较小堆顶的右孩子和另一个堆合并，然后进行无条件交换
     * @param h1
     * @param h2
     * @return
     */
    private  SkewHeapNode<T> recursiveMerge(SkewHeapNode<T> h1,SkewHeapNode<T> h2){
        if(h1 == null) return h2;
        if(h2 == null) return h1;
        //保证h1是具有较小根的堆
        if(h1.element.compareTo(h2.element) > 0){
            SkewHeapNode<T> tmp = h1;
            h1 = h2;
            h2 = tmp;
        }
        h1.right = recursiveMerge(h1.right,h2);
        SkewHeapNode<T> tmp = h1.left;
        h1.left = h1.right;
        h1.right = tmp;
        return h1;
    }

    /**
     * 利用栈记录堆顶元素小的堆的堆顶
     * @param h1
     * @param h2
     * @return
     */
    private  SkewHeapNode<T> iterativeMerge(SkewHeapNode<T> h1,SkewHeapNode<T> h2){
        if(h1 == null) return h2;
        if(h2 == null) return h1;

        Deque<SkewHeapNode<T>> stack = new LinkedList<>();

        while (h1 != null){
            if(h1.element.compareTo(h2.element) > 0){
                SkewHeapNode<T> tmp = h1;
                h1 = h2;
                h2 = tmp;
            }
            stack.push(h1);
            h1 = h1.right;
        }

        while (!stack.isEmpty()){
            SkewHeapNode<T> pop = stack.pop();
            pop.right = pop.left;
            pop.left = h2;
            h2 = pop;
        }
        return h2;

    }


    private static class SkewHeapNode<T> {
        private T element;
        private SkewHeapNode<T> left;
        private SkewHeapNode<T> right;

        public SkewHeapNode(T element, SkewHeapNode<T> left, SkewHeapNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {
        SkewHeap<Integer> heap = new SkewHeap<>();
        for(int i = 0; i< 100; i++){
            heap.insert(ThreadLocalRandom.current().nextInt(1000));
        }
        SkewHeap<Integer> heap1 = new SkewHeap<>();
        for(int i = 0; i< 100; i++){
            heap1.insert(ThreadLocalRandom.current().nextInt(1000));
        }
        heap.merge(heap1);
        for(int i =0; i < 200; i++){
            System.out.println(heap.deleteMin());
        }
    }

}
