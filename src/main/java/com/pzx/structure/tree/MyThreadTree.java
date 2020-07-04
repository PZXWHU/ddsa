package com.pzx.structure.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 线索二叉树
 * 对于n个结点的二叉树，在二叉链存储结构中有n+1个空链域，利用这些空链域存放在某种遍历次序下该结点的前驱结点和后继结点的指针，这些指针称为线索，加上线索的二叉树称为线索二叉树。
 * 根据线索性质的不同，线索二叉树可分为前序线索二叉树、中序线索二叉树和后序线索二叉树三种。
 *
 * 记node指向二叉链表中的一个结点，以下是建立线索的规则：
 *     （1）如果node的左指针域为空，则存放指向某种遍历序列中该结点的前驱结点，这个结点称为node的前驱；
 *     （2）如果node的右指针域为空，则存放指向某种遍历序列中该结点的后继结点。这个结点称为node的后继；
 *
 *参考：https://blog.csdn.net/jisuanjiguoba/article/details/81092812
 */
public class MyThreadTree<T extends Comparable<? super T>> {


    private ThreadTreeNode<T> root;
    private ThreadTreeNode<T> preNode;   //线索化时记录前一个节点


    /**
     * 中序线索化二叉树
     * @param node  节点
     */
    void inThreadOrder(ThreadTreeNode<T> node) {

        if(node == null) {
            return;
        }

        //处理左子树
        inThreadOrder(node.left);

        //左指针为空,将左指针指向前驱节点
        if(node.left == null) {
            node.left = preNode;
            node.isLeftThread = true;
        }
        //前一个节点的后继节点指向当前节点
        if(preNode != null && preNode.right == null) {
            preNode.right = node;
            preNode.isRightThread = true;
        }
        preNode = node;
        //处理右子树
        inThreadOrder(node.right);
    }

    /**
     * 中序遍历线索二叉树，按照后继方式遍历（思路：找到最左子节点开始）
     * @param node
     */
    private List<T> inThreadList(ThreadTreeNode<T> node) {
        List<T> resultList = new ArrayList<>();

        //1、找中序遍历方式开始的节点
        while(node != null && !node.isLeftThread) {
            node = node.left;
        }
        while(node != null) {
            resultList.add(node.element);

            //如果右指针是线索
            if(node.isRightThread) {
                node = node.right;
            } else {    //如果右指针不是线索，找到右子树开始的节点
                node = node.right;
                while(node != null && !node.isLeftThread) {
                    node = node.left;
                }
            }
        }
        return resultList;

    }



    private static class ThreadTreeNode<T> {
        T element;        //数据域
        ThreadTreeNode<T> left;          //左指针域
        ThreadTreeNode<T> right;         //右指针域
        boolean isLeftThread = false;   //左指针域类型  false：指向子节点、true：前驱线索
        boolean isRightThread = false;  //右指针域类型  false：指向子节点、true：后继线索

        public ThreadTreeNode(T element, ThreadTreeNode<T> left, ThreadTreeNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }
}
