package com.pzx.structure.tree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 二叉平衡树，每个节点的左右子树高度差不能超过1，否则需要旋转来恢复平衡
 * 插入删除递归版本，非递归版本可能需要在节点类中增加parent指针，或者使用栈结构，用于插入之后更新高度、调整平衡
 * @param <T>
 */
public class AVLTree<T extends Comparable<? super T>> {

    private AVLTreeNode<T> root;

    public AVLTree() {
    }

    private static <T> int height(AVLTreeNode<T> treeNode){
        return treeNode == null ? -1 : treeNode.height;
    }

    public void insert(T element){
        Objects.requireNonNull(element);
        root = insert(element, root);
    }

    /**
     *二叉查找树插入（递归） +  插入之后平衡
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
     * 1.左左  右旋转   （插入左孩子的左子树，与右子树不平衡）
     * 2.右右  左旋转   （插入右孩子的右子树，与左子树不平衡）
     * 3.左右  双次右旋  先左旋转后右旋转    （插入左孩子的右子树，与右子树不平衡）
     * 4.右左  双次左旋  先右旋转再左旋转    （插入右孩子的左子树，与左子树不平衡）
     * @param treeNode
     * @return
     */
    private AVLTreeNode<T> balance(AVLTreeNode<T> treeNode){
        if(treeNode == null)
            return treeNode;

        if(height(treeNode.left) - height(treeNode.right) > 1){
            if(height(treeNode.left.left) >= height(treeNode.left.right)){ //使用等于号是为了在删除元素时也能正确使用balance方法，添加元素时两者不可能会相同
                treeNode = rightRotate(treeNode);
            }else {
                treeNode = leftAndRightRotate(treeNode);
            }
        }else if(height(treeNode.right) - height(treeNode.left) > 1){
            if(height(treeNode.right.right) >= height(treeNode.right.left)){//使用等于号是为了在删除元素时也能正确使用balance方法，添加元素时两者不可能会相同
                treeNode = leftRotate(treeNode);
            }else {
                treeNode = rightAndLeftRotate(treeNode);
            }
        }
        treeNode.height = Math.max(height(treeNode.left), height(treeNode.right)) + 1;

        return treeNode;
    }

    private AVLTreeNode<T> rightRotate(AVLTreeNode<T> treeNode){
        AVLTreeNode<T> rotateNode = treeNode.left;
        treeNode.left = rotateNode.right;
        rotateNode.right = treeNode;

        treeNode.height = Math.max(height(treeNode.left), height(treeNode.right))+1;
        rotateNode.height = Math.max(height(rotateNode.left), height(rotateNode.right))+1;

        return rotateNode;
    }

    /**
     * 双旋转可由两次单旋转组合而成
     * @param treeNode
     * @return
     */
    private AVLTreeNode<T> leftAndRightRotate(AVLTreeNode<T> treeNode){
        treeNode.left = leftRotate(treeNode.left);
        return rightRotate(treeNode);
    }
    private AVLTreeNode<T> leftRotate(AVLTreeNode<T> treeNode){
        AVLTreeNode<T> rotateNode = treeNode.right;
        treeNode.right = rotateNode.left;
        rotateNode.left = treeNode;

        treeNode.height = Math.max(height(treeNode.left), height(treeNode.right))+1;
        rotateNode.height = Math.max(height(rotateNode.left), height(rotateNode.right))+1;

        return rotateNode;
    }
    private AVLTreeNode<T> rightAndLeftRotate(AVLTreeNode<T> treeNode){
        treeNode.right = rightRotate(treeNode.right);
        return leftRotate(treeNode);
    }

    public void remove(T element){
        Objects.requireNonNull(element);
        root = remove(element, root);
    }

    /**
     * 二叉查找树的删除方法（递归） + 删除之后调整平衡
     * @param element
     * @param treeNode
     * @return 返回删除之后的子树根节点
     */
    private AVLTreeNode<T> remove(T element, AVLTreeNode<T> treeNode){
        if(treeNode == null)
            return treeNode;//删除节点不存在

        int compareResult = element.compareTo(treeNode.element);
        if(compareResult > 0){
            treeNode.right = remove(element, treeNode.right);
        }else if(compareResult < 0){
            treeNode.left = remove(element, treeNode.left);
        }else if(treeNode.left != null && treeNode.right != null){
            //treeNode.element = findMin(treeNode.right).element;
            //treeNode.right = remove(element, treeNode.right);
            treeNode.element = findAndRemoveRightSubTreeMin(treeNode).element;
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

    /**
     * 找到右子树中最小节点，返回并在子树中删除此节点
     * @param parentTreeNode
     * @return
     */
    private AVLTreeNode<T> findAndRemoveRightSubTreeMin(AVLTreeNode<T> parentTreeNode){
        AVLTreeNode<T> minNode = null;
        if(parentTreeNode != null && parentTreeNode.right != null){
            if(parentTreeNode.right.left == null){
                minNode = parentTreeNode.right;
                parentTreeNode.right = remove(parentTreeNode.right.element, parentTreeNode.right);
            }
            else {
                parentTreeNode = parentTreeNode.right;
                while (parentTreeNode.left.left != null){
                    parentTreeNode = parentTreeNode.left;
                }
                minNode = parentTreeNode.left;
                parentTreeNode.left = remove(parentTreeNode.left.element, parentTreeNode.left);
            }
        }

        return minNode;

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
        AVLTree<Integer> tree = new AVLTree<>();
        for(int i = 0; i<1000; i++){
            tree.insert(ThreadLocalRandom.current().nextInt(1000));
        }
        for(int i = 0; i<500; i++){
            tree.remove(ThreadLocalRandom.current().nextInt(1000));
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
