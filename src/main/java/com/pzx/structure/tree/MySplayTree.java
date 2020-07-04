package com.pzx.structure.tree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 伸展树是一种自调整形式的二叉查找树,对元素的每一次访问，都将把元素移动到根节点，并且把访问路径上的节点都提高
 * 它的优势在于不需要记录用于平衡树的冗余信息
 * @param <T>
 */
public class MySplayTree<T extends Comparable<? super T>> {

    private SplayTreeNode<T> root;

    public MySplayTree(){

    }

    /**
     * 二叉查找树非递归插入 +  对插入的元素进行展开
     * @param element
     */
    public void insert(T element){
        Objects.requireNonNull(element);
        if(root == null){
            root = new SplayTreeNode<>(element, null, null);
            return;
        }

        SplayTreeNode<T> node = root;
        while (true){
            int compareResult = element.compareTo(node.element);
            if(compareResult > 0){
                if(node.right == null){
                    node.right = new SplayTreeNode<>(element, null, null);
                    break;
                }
                node = node.right;
            }else if(compareResult < 0){
                if (node.left == null){
                    node.left = new SplayTreeNode<>(element, null, null);
                    break;
                }
                node = node.left;
            }else {
                break;
            }
        }

        root = splay(element, root);
    }

    /**
     * 如果要删除的节点存在
     * 1.将要删除的元素旋转到根节点
     * 2.如果根节点的左孩子为空，则将根节点的右孩子作为根节点
     * 3.如果根节点的右孩子为空，则将根节点的左孩子作为根节点
     * 4.如果根结点有两个孩子，先把根结点左子树中最大的节点旋转到左孩子的位置（这样做保证了左孩子的右孩子为空），然后把根结点的右孩子挂接到左孩子的右孩子位置。
     * @param element
     */
    public void remove(T element){

        if(contains(element)){
            SplayTreeNode<T> node;
            if(root.left != null){
                node = splay(findMax(root.left).element, root.left);
                node.right = root.right;
            }else {
                node = root.right;
            }
            root = node;
        }
    }

    /**
     *
     * @param element
     */
    public boolean contains(T element){
        Objects.requireNonNull(element);
        if(root == null)
            return false;

        root = splay(element, root);
        if(root.element.compareTo(element) == 0)
            return true;
        else
            return false;
    }

    private SplayTreeNode<T> findMax(SplayTreeNode<T> treeNode){
        if(treeNode != null){
            while (treeNode.right != null)
                treeNode = treeNode.right;
        }
        return treeNode;
    }

    /**
     * 对树进行展开，返回展开后的根节点
     * @param element
     * @param node
     * @return
     */
    public SplayTreeNode<T> splay(T element, SplayTreeNode<T> node){
        Objects.requireNonNull(element);
        if (node == null)
            return node;

        Deque<SplayTreeNode<T>> stack = new LinkedList<>();
        while (true){
            int compareResult = element.compareTo(node.element);
            if (compareResult > 0 && node.right != null){
                stack.push(node);
                node = node.right;
            }else if ((compareResult < 0 && node.left != null)){
                stack.push(node);
                node = node.left;
            }else if(compareResult == 0){
                node = splayTreeRotate(node, stack);//对树进行伸展
                break;
            }else {
                break;
            }
        }
        return node;
    }


    /**
     * 对树进行旋转。对于访问路径上的所有节点：
     * 1.如果此节点存在父节点但不存在祖父节点，则进行一次单旋转
     * 2.如果此节点存在父节点也存在祖父节点，则判断三个节点的关系类型
     * 2.1 如果是直线型，则进行当前节点和祖父节点的旋转（连续两次相同方向的单旋转，祖父节点单旋转+父节点单旋转具体旋转方式看代码）
     * 2.2 如果是之字形，则进行类型AVL的双旋转（两次方向不同的单旋转，父节点单旋转+祖父节点单旋转，具体旋转方式看代码）
     * @param node
     * @param stack
     */
    private SplayTreeNode<T> splayTreeRotate(SplayTreeNode<T> node, Deque<SplayTreeNode<T>> stack){

        if (stack.isEmpty())
            return node;

        while (true){
            SplayTreeNode<T> parentNode = stack.pop();
            if(stack.isEmpty()){
                //没有祖父节点，则和父节点（根节点）进行单旋转
                int compareResult = node.element.compareTo(parentNode.element);
                if(compareResult > 0)
                    return leftRotate(parentNode);
                else
                    return rightRotate(parentNode);
            }else {
                SplayTreeNode<T> grandParentNode = stack.pop();
                int compareResult1 = node.element.compareTo(parentNode.element);
                int compareResult2 = parentNode.element.compareTo(grandParentNode.element);
                if(compareResult1 >0 && compareResult2 > 0){
                    node = leftDoubleRotate(grandParentNode);
                }else if(compareResult1 < 0 && compareResult2 <0){
                    node = rightDoubleRotate(grandParentNode);
                }else if(compareResult1 < 0 && compareResult2 >0){
                    node = rightAndLeftRotate(grandParentNode);
                }else{
                    node = leftAndRightRotate(grandParentNode);
                }
                if(stack.isEmpty())
                    return node;
                else {
                    int compareResult = node.element.compareTo(stack.peek().element);
                    if(compareResult > 0)
                        stack.peek().right = node;
                    else
                        stack.peek().left = node;
                }

            }
        }

    }

    private SplayTreeNode<T> leftRotate(SplayTreeNode<T> node){
        SplayTreeNode<T> rotateNode = node.right;
        node.right = rotateNode.left;
        rotateNode.left = node;
        return rotateNode;
    }
    private SplayTreeNode<T> rightRotate(SplayTreeNode<T> node){
        SplayTreeNode<T> rotateNode = node.left;
        node.left = rotateNode.right;
        rotateNode.right = node;
        return rotateNode;
    }
    private SplayTreeNode<T> rightAndLeftRotate(SplayTreeNode<T> node){
        node.right = rightRotate(node.right);
        return leftRotate(node);
    }
    private SplayTreeNode<T> leftAndRightRotate(SplayTreeNode<T> node){
        node.left = leftRotate(node.left);
        return rightRotate(node);
    }

    private SplayTreeNode<T> leftDoubleRotate(SplayTreeNode<T> node){
        return leftRotate(leftRotate(node));
    }

    private SplayTreeNode<T> rightDoubleRotate(SplayTreeNode<T> node){
        return rightRotate(rightRotate(node));
    }

    private static class SplayTreeNode<T>{
        public T element;
        public SplayTreeNode<T> left;
        public SplayTreeNode<T> right;

        public SplayTreeNode(T element, SplayTreeNode<T> left, SplayTreeNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {
        MySplayTree<Integer> tree = new MySplayTree<>();
        /*
        for(int i = 0; i<1000; i++){
            tree.insert(ThreadLocalRandom.current().nextInt(1000));
        }
        for(int i = 0; i<500; i++){
            tree.remove(ThreadLocalRandom.current().nextInt(1000));
        }

        //中序遍历，判断是否符合顺序条件
        int tmp = 0;
        Deque<SplayTreeNode<Integer>> stack = new LinkedList<>();
        SplayTreeNode<Integer> node = tree.root;
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

         */

        tree.root = new SplayTreeNode<>(7,null,null);
        SplayTreeNode<Integer> node = tree.root;
        for(int i = 6; i>0; i--){
            node.left = new SplayTreeNode<>(i,null,null);
            node = node.left;
        }
        if(tree.contains(1)){
            //层次遍历，展开是否正确
            Queue<SplayTreeNode<Integer>> queue = new LinkedList<>();
            queue.offer(tree.root);
            while (!queue.isEmpty()){
                SplayTreeNode<Integer> treeNode = queue.poll();
                System.out.println(treeNode.element);
                if(treeNode.left != null)
                    queue.offer(treeNode.left);
                if(treeNode.right != null)
                    queue.offer(treeNode.right);
            }
        }

    }

}
