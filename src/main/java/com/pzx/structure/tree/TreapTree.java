package com.pzx.structure.tree;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Treap树
 * 树的节点中存在一个优先级，树节点的优先级满足堆序性质：任何节点的优先级大于等于它父节点的优先级
 * 查找、插入、删除的期望时间为O（logN）
 */
public class TreapTree<T extends Comparable<? super T>> {

    private static final int MAX_PRIORITY = Integer.MAX_VALUE;
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private TreapTreeNode<T> nullNode;
    private TreapTreeNode<T> root;

    public TreapTree() {
        nullNode = new TreapTreeNode<>(null,null,null);
        nullNode.priority = MAX_PRIORITY;
        nullNode.left = nullNode.right = nullNode;
        root = nullNode;
    }

    public void insert(T element){
        root = insert(element, root);
    }

    public void remove(T element){
        root = remove(element, root);
    }

    /**
     * 新插入的数据将创建一个新的叶节点。将此节点进行向上旋转，直到它的优先级满足堆序性质为止。
     * @param element
     * @param treeNode
     * @return
     */
    private TreapTreeNode<T> insert(T element, TreapTreeNode<T> treeNode){
        if (treeNode == nullNode)
            return new TreapTreeNode<>(element, nullNode, nullNode);

        int compareResult = element.compareTo(treeNode.element);

        if(compareResult < 0){
            treeNode.left = insert(element, treeNode.left);
            if(treeNode.left.priority < treeNode.priority)
                treeNode = rightRotate(treeNode);
        }else if(compareResult > 0){
            treeNode.right = insert(element, treeNode.right);
            if(treeNode.right.priority < treeNode.priority)
                treeNode = leftRotate(treeNode);
        }
        return treeNode;
    }

    /**
     * 当找到要删除的节点时，将其向下旋转至叶节点进行删除。
     * @param element
     * @param treeNode
     * @return
     */
    private TreapTreeNode<T> remove(T element, TreapTreeNode<T> treeNode){
        if (treeNode != nullNode){
            int compareResult = element.compareTo(treeNode.element);
            if (compareResult < 0)
                treeNode.left = remove(element, treeNode.left);
            else if(compareResult > 0)
                treeNode.right = remove(element, treeNode.right);
            else {
                if(treeNode.left.priority < treeNode.right.priority)
                    treeNode = rightRotate(treeNode);
                else
                    treeNode = leftRotate(treeNode);//当删除节点的两个子节点为nullNode时也会进行左旋

                if (treeNode != nullNode)
                    treeNode = remove(element, treeNode);
                else
                    treeNode.left = nullNode;//将要删除的节点进行替换成nullNode
            }
        }
        return treeNode;
    }



    private TreapTreeNode<T> rightRotate(TreapTreeNode<T> treeNode){
        TreapTreeNode<T> rotateNode = treeNode.left;
        treeNode.left = rotateNode.right;
        rotateNode.right = treeNode;
        return rotateNode;
    }

    private TreapTreeNode<T> leftRotate(TreapTreeNode<T> treeNode){
        TreapTreeNode<T> rotateNode = treeNode.right;
        treeNode.right = rotateNode.left;
        rotateNode.left = treeNode;
        return rotateNode;
    }

    public class TreapTreeNode<T>{
        private T element;
        TreapTreeNode<T> left;
        TreapTreeNode<T> right;
        int priority;

        public TreapTreeNode(T element, TreapTreeNode<T> left, TreapTreeNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.priority = random.nextInt();
        }
    }

}
