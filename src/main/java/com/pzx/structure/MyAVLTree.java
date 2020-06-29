package com.pzx.structure;

import javax.swing.tree.TreeNode;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 二叉平衡树 递归版本
 * 非递归版本可能需要在节点类中增加parent指针，用于插入之后更新高度、调整平衡
 * @param <T>
 */
public class MyAVLTree<T extends Comparable<? super T>> {

    private AVLTreeNode<T> root;

    public MyAVLTree() {
    }

    private static <T> int height(AVLTreeNode<T> treeNode){
        return treeNode == null ? -1 : treeNode.height;
    }

    public void insert(T element){
        Objects.requireNonNull(element);
        root = insert(element, root);
    }

    /**
     *
     * @param element
     * @param treeNode
     * @return 返回插入后的子树的根节点
     */
    private AVLTreeNode<T> insert(T element, AVLTreeNode<T> treeNode){

        if(treeNode == null)
            return new AVLTreeNode<>(element, null, null);

        int compareResult = element.compareTo(treeNode.element);
        if(compareResult > 0){
            treeNode.right = insert(element, treeNode.right);
        }else if (compareResult < 0){
            treeNode.left = insert(element, treeNode.left);
        }

        return balance(treeNode);
    }

    /**
     * 根据插入节点位置不同，使用不同的旋转：
     * 1.左左  右旋转
     * 2.右右  左旋转
     * 3.左右  双次右旋  先左旋转后右旋转
     * 4.右左  双次左旋  先右旋转再左旋转
     * @param treeNode
     * @return
     */
    private AVLTreeNode<T> balance(AVLTreeNode<T> treeNode){
        if(treeNode == null)
            return treeNode;

        if(height(treeNode.left) - height(treeNode.right) > 1){
            if(height(treeNode.left.left) >= height(treeNode.left.right)){ //使用等于号是为了在删除元素时也能正确使用balance方法，添加元素时两者不可能会相同
                treeNode = rotateWithLeftChild(treeNode);
            }else {
                treeNode = doubleRotateWithLeftChild(treeNode);
            }
        }else if(height(treeNode.right) - height(treeNode.left) > 1){
            if(height(treeNode.right.right) >= height(treeNode.right.left)){//使用等于号是为了在删除元素时也能正确使用balance方法，添加元素时两者不可能会相同
                treeNode = rotateWithRightChild(treeNode);
            }else {
                treeNode = doubleRotateWithRightChild(treeNode);
            }
        }
        treeNode.height = Math.max(height(treeNode.left), height(treeNode.right)) + 1;

        return treeNode;
    }

    private AVLTreeNode<T> rotateWithLeftChild(AVLTreeNode<T> treeNode){
        AVLTreeNode<T> rotateNode = treeNode.left;
        treeNode.left = rotateNode.right;
        rotateNode.right = treeNode;

        treeNode.height = Math.max(height(treeNode.left), height(treeNode.right))+1;
        rotateNode.height = Math.max(height(rotateNode.left), height(rotateNode.right))+1;

        return rotateNode;
    }

    /**
     * 双旋转可由一次左旋加一次右旋组合而成
     * @param treeNode
     * @return
     */
    private AVLTreeNode<T> doubleRotateWithLeftChild(AVLTreeNode<T> treeNode){
        treeNode.left = rotateWithRightChild(treeNode.left);
        return rotateWithLeftChild(treeNode);
    }
    private AVLTreeNode<T> rotateWithRightChild(AVLTreeNode<T> treeNode){
        AVLTreeNode<T> rotateNode = treeNode.right;
        treeNode.right = rotateNode.left;
        rotateNode.left = treeNode;

        treeNode.height = Math.max(height(treeNode.left), height(treeNode.right))+1;
        rotateNode.height = Math.max(height(rotateNode.left), height(rotateNode.right))+1;

        return rotateNode;
    }
    private AVLTreeNode<T> doubleRotateWithRightChild(AVLTreeNode<T> treeNode){
        treeNode.right = rotateWithLeftChild(treeNode.right);
        return rotateWithRightChild(treeNode);
    }

    public void remove(T element){
        Objects.requireNonNull(element);
        root = remove(element, root);
    }

    /**
     * 二叉查找树的删除方法 + 删除之后调整平衡
     * @param element
     * @param treeNode
     * @return 返回删除之后的子树根节点
     */
    private AVLTreeNode<T> remove(T element, AVLTreeNode<T> treeNode){
        if(treeNode == null)
            return treeNode;

        int compareResult = element.compareTo(treeNode.element);
        if(compareResult > 0){
            treeNode.right = remove(element, treeNode.right);
        }else if(compareResult < 0){
            treeNode.left = remove(element, treeNode.left);
        }else if(treeNode.left != null && treeNode.right != null){
            treeNode.element = findMin(treeNode.right).element;
            treeNode.right = remove(element, treeNode.right);
        }else {
            treeNode = treeNode.left != null ? treeNode.left : treeNode.right;
        }

        return balance(treeNode);
    }

    private AVLTreeNode<T> findMin(AVLTreeNode<T> treeNode){
        if(treeNode != null){
            while (treeNode.left != null)
                treeNode = treeNode.left;
        }
        return treeNode;
    }

    private AVLTreeNode<T> findMax(AVLTreeNode<T> treeNode){
        if(treeNode != null){
            while (treeNode.right != null)
                treeNode = treeNode.right;
        }
        return treeNode;
    }


    private static class AVLTreeNode<T>{
        public T element;
        public AVLTreeNode<T> left;
        public AVLTreeNode<T> right;
        public int height;

        public AVLTreeNode(T element, AVLTreeNode<T> left, AVLTreeNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.height = 0;
        }
    }

    public static void main(String[] args) {
        MyAVLTree<Integer> tree = new MyAVLTree<>();
        for(int i = 0; i<1000; i++){
            tree.insert(ThreadLocalRandom.current().nextInt(1000));
        }

        //层次遍历，是否符合平衡条件
        Queue<AVLTreeNode<Integer>> queue = new LinkedList<>();
        queue.offer(tree.root);
        while (!queue.isEmpty()){
            AVLTreeNode<Integer> treeNode = queue.poll();
            if(treeNode.left != null && treeNode.right != null){
                if(Math.abs(height(treeNode.left) - height(treeNode.right)) > 1){
                    System.out.println("error! 不符合平衡条件");
                    break;
                }
                queue.offer(treeNode.left);queue.offer(treeNode.right);
            }
        }

        //中序遍历，判断是否符合顺序条件
        int tmp = 0;
        Deque<AVLTreeNode<Integer>> stack = new LinkedList<>();
        AVLTreeNode<Integer> node = tree.root;
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
