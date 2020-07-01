package com.pzx.algorithm;

import java.util.*;

/**
 * 已知中序遍历和前序或者后序遍历，求所构成的二叉树
 */
public class CreateTreeAccordingInOrder {


    /**
     * 中序和前序求二叉树
     * 前序第一个元素为根节点，在中序中找到根节点，将中序分为两个部分，根节点左方为左子树和根节点右方右子树
     * 将前序也分成两个部分，根据左子树和右子树的大小，前序序列第一个为根节点，紧接着为左子树序列，然后为右子树序列
     * 递归执行此方法，求出左子树和右子树。
     * @param inOrder
     * @param preOrder
     * @param <T>
     * @return
     */
    private static <T> TreeNode<T>  createTreeAccordingInorderAndPreOrder(List<T> inOrder, List<T> preOrder){
        if (inOrder.size() != preOrder.size() || inOrder.size() == 0)
            return null;

        int inOrderLength = inOrder.size();
        TreeNode<T> root = new TreeNode<>(preOrder.get(0), null, null);
        int rootInOrderIndex = inOrder.indexOf(root.element);

        root.left = createTreeAccordingInorderAndPreOrder(inOrder.subList(0,rootInOrderIndex), preOrder.subList(1,rootInOrderIndex+1));
        root.right = createTreeAccordingInorderAndPreOrder(inOrder.subList(rootInOrderIndex + 1, inOrderLength), preOrder.subList(rootInOrderIndex + 1, inOrderLength));
        return root;
    }

    /**
     * 中序和后序求二叉树
     * 原理与上述相同
     * 后序中最后一个为根节点，将中序分为左子树和右子树，根据左右子树大小，将后序也分为左子树和右子树（后序 = 左子树 + 右子树 +根节点），递归执行此方法
     * @param inOrder
     * @param postOrder
     * @param <T>
     * @return
     */
    private static <T> TreeNode<T>  createTreeAccordingInorderAndPostOrder(List<T> inOrder, List<T> postOrder){
        if (inOrder.size() != postOrder.size() || inOrder.size() == 0)
            return null;

        int inOrderLength = inOrder.size();
        TreeNode<T> root = new TreeNode<>(postOrder.get(inOrderLength - 1), null, null);
        int rootInOrderIndex = inOrder.indexOf(root.element);

        root.left = createTreeAccordingInorderAndPostOrder(inOrder.subList(0,rootInOrderIndex), postOrder.subList(0,rootInOrderIndex));
        root.right = createTreeAccordingInorderAndPostOrder(inOrder.subList(rootInOrderIndex + 1, inOrderLength), postOrder.subList(rootInOrderIndex, inOrderLength - 1));
        return root;
    }


    private static class TreeNode<T>{
        public T element;
        public TreeNode<T> left;
        public TreeNode<T> right;

        public TreeNode(T element, TreeNode<T> left, TreeNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {
        /**
        List<Integer> preOrder = Arrays.asList(1,2,4,8,9,5,10,3,6,7);
        List<Integer> inOrder = Arrays.asList(8,4,9,2,10,5,1,6,3,7);
        TreeNode<Integer> node = CreateTreeAccordingInOrder.createTreeAccordingInorderAndPreOrder(inOrder, preOrder);
         */
        List<Integer> postOrder = Arrays.asList(9,15,7,20,3);
        List<Integer> inOrder = Arrays.asList(9,3,15,20,7);
        TreeNode<Integer> node = CreateTreeAccordingInOrder.createTreeAccordingInorderAndPostOrder(inOrder, postOrder);
        Queue<TreeNode<Integer>> queue = new LinkedList<>();
        queue.offer(node);
        while (!queue.isEmpty()){
            node = queue.poll();
            System.out.println(node.element);
            if (node.left != null)
                queue.offer(node.left);
            if(node.right != null)
                queue.offer(node.right);
        }

    }
}
