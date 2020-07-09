package com.pzx.structure.tree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class BinarySearchTree<T extends Comparable<? super T>> {

    private BinarySearchTreeNode<T> root;

    /**
     * 二叉查找树非递归插入
     * @param element
     */
    public void insert(T element){
        Objects.requireNonNull(element);
        if(root == null){
            root = new BinarySearchTreeNode<>(element, null, null);
            return;
        }

        BinarySearchTreeNode<T> node = root;
        while (true){
            int compareResult = element.compareTo(node.element);
            if(compareResult > 0){
                if(node.right == null){
                    node.right = new BinarySearchTreeNode<>(element, null, null);
                    return;
                }
                node = node.right;
            }else if(compareResult < 0){
                if (node.left == null){
                    node.left = new BinarySearchTreeNode<>(element, null, null);
                    return;
                }
                node = node.left;
            }else {
                return;
            }
        }


    }

    /**
     * 二叉查找树非递归删除
     * 如果删除节点有一个子结点，则用子节点替换该节点
     * 如果删除节点有两个子节点，则在右子树中找到最小元素代替当前节点，然后在右子树中删除最小节点
     * @param element
     */
    public void remove(T element){
        Objects.requireNonNull(element);
        if (root == null || element.compareTo(root.element) == 0){
            root = null;
            return;
        }

        BinarySearchTreeNode<T> node = root;
        BinarySearchTreeNode<T> parentNode = null;

        while (true){
            int compareResult = element.compareTo(node.element);
            if(compareResult > 0 && node.right != null){
                parentNode = node;
                node = node.right;
            }else if(compareResult < 0 && node.left != null){
                parentNode = node;
                node = node.left;
            }else if(compareResult == 0){
                if(node.left != null && node.right != null){
                    BinarySearchTreeNode<T> replaceNode = node.right;
                    while (replaceNode.left != null){
                        parentNode = replaceNode;
                        replaceNode = replaceNode.left;
                    }
                    node.element = replaceNode.element;
                    //转换删除目标
                    node = replaceNode;
                    element = replaceNode.element;

                }else {
                    compareResult = node.element.compareTo(parentNode.element);
                    if(compareResult > 0) //确定是父节点的左孩子还是右孩子
                        parentNode.right = node.left != null ? node.left : node.right;
                    else
                        parentNode.left = node.left != null ? node.left : node.right;
                    return;
                }
            }else{
                return;
            }

        }

    }

    /**
     * 伸展树访问元素之后，树结构需要进行展开
     * @param element
     */
    public boolean contains(T element){
        Objects.requireNonNull(element);
        if(root == null)
            return false;

        BinarySearchTreeNode<T> node = root;
        while (true){
            int compareResult = element.compareTo(node.element);
            if (compareResult > 0 && node.right != null){
                node = node.right;
            }else if ((compareResult < 0 && node.left != null)){
                node = node.left;
            }else if(compareResult == 0){
                return true;
            }else {
                return false;
            }
        }
    }

    private static class BinarySearchTreeNode<T>{
        public T element;
        public BinarySearchTreeNode<T> left;
        public BinarySearchTreeNode<T> right;

        public BinarySearchTreeNode(T element, BinarySearchTreeNode<T> left, BinarySearchTreeNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        for(int i = 0; i<1000; i++){
            tree.insert(ThreadLocalRandom.current().nextInt(1000));
        }
        for(int i = 0; i<500; i++){
            tree.remove(ThreadLocalRandom.current().nextInt(1000));
        }
        //中序遍历，判断是否符合顺序条件
        int tmp = 0;
        Deque<BinarySearchTreeNode<Integer>> stack = new LinkedList<>();
        BinarySearchTreeNode<Integer> node = tree.root;
        while (node != null || !stack.isEmpty()){
            while (node != null){
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            if(node.element < tmp){
                System.out.println("error! 不符合顺序条件");
                break;
            }
            node = node.right;
        }
    }


}
