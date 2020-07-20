package com.pzx.structure.tree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * 红黑树
 * 特性：
 * 1、每个节点是红色或者是黑色
 * 2、根节点为黑色
 * 3、所有叶子节点都是黑色(叶子是null节点)
 * 4、树不能有连续的红节点
 * 5、从任一节点到其每个叶子节点的所有简单路径都包含相同数目的黑色节点
 *
 * 推论：
 * 1） 由性质4）和性质5），最长路径至多为最短路径2倍（最短路径为全黑，最长路径为红黑相间）;
 * 2） 由推论1），红黑数高度最多为2log(N+1)。
 */
public class RedBlackTree<T extends Comparable<? super T>> {

    private static final int RED = 0;
    private static final int BLACK = 1;

    //为了方便插入的时候判断
    private RedBlackTreeNode<T> header;
    private RedBlackTreeNode<T> nullNode;

    public RedBlackTree() {
        nullNode = new RedBlackTreeNode<>(null);
        nullNode.left = nullNode.right = nullNode;
        header = new RedBlackTreeNode<>(null);
        header.left = header.right = nullNode;
    }

    /**
     * 自底向上的插入，插入节点必须是红色，不违反红黑树第五条性质
     * 1、当插入父节点是黑色时，插入成功，返回。
     * 2、当插入父节点是红色时，父节点的兄弟节点是黑色或是不存在时，此时祖父节点必定是黑色。
     *      根据插入节点与父节点和祖父节点的结构类型（之字型或者一字型）进行旋转以及变色，保证旋转之后的子树的根为黑色。
     * 3、当插入父节点是红色时，父节点的兄弟节点是红色，此时祖父节点必定是黑色。
     *      根据插入节点与父节点和祖父节点的结构类型（之字型或者一字型）进行旋转，但是变色和2不同，旋转之后的子树的根为红色。
     *      因为旋转变色之后子树的根为红色，其父节点可能为红色，所以要对子树的根继续以此方法向上过滤（需要父链或者是栈实现）。
     *
     */
    public void insertBottomUp(){

    }

    /**
     *
     * 自顶向下的插入：
     * 主要思想是，在向下的查找过程中，保证一个节点不会有两个红色子节点，则最后插入新节点时，只会发生自底向上插入中的1、2情况，就不需要向上过滤的步骤，避免了情况3。
     *
     * 1、在自顶向下的查找的过程中，判断路径上的节点是否具有两个红色子节点
     * 2、如果是，则将其子节点均变为黑色，自己变成红色。判断父节点是否为红色，若为红色，则进行旋转变色（同自底向上的情况2）。
     * 3、当到达叶节点，需要插入新节点时，判断父节点是否为红色，若为红色，则进行旋转变色（同自底向上的情况2）。
     * @param element
     */
    public void insert(T element){
        if (element == null)
            return;

        RedBlackTreeNode<T> current;
        RedBlackTreeNode<T> parent;
        RedBlackTreeNode<T> grand;
        RedBlackTreeNode<T> great;
        current = parent = grand = great = header;
        nullNode.element = element;

        while (compare(element, current) != 0){
            great = grand;grand = parent; parent = current;
            current = compare(element, current) < 0 ? current.left : current.right;
            if (current.left.color == RED && current.right.color == RED){
                handleReorient(element, current, parent, grand, great);
            }
        }

        if(current != nullNode)
            return;

        //默认节点是黑色，但是最后调用的handleReorient会将其转为黑色
        current = new RedBlackTreeNode<>(element, nullNode, nullNode);

        if (compare(element, parent) < 0)
            parent.left = current;
        else
            parent.right = current;

        //将当前节点转为黑色，且判断父节点是否为红色，并进行旋转变色
        handleReorient(element, current, parent, grand, great);
    }


    /**
     * https://blog.csdn.net/javyzheng/article/details/12339463?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.nonecase
     *
     * 自顶而下的删除
     * 主要思想是，在向下查找的过程中保证当前节点为红色，则当找到要删除的叶节点时，直接删除即可，因为删除红色节点不会破坏红黑树的性质。
     *
     * 1、当删除节点有两个孩子时，用右子树最小节点代替（将右子树最小节点内容放入此节点），删除该最小节点（此节点必然最多有一个儿子）。
     * 2、当删除节点有一个右孩子时，以1）方式删除。
     * 3、当删除节点有一个左孩子时，用左子树最大节点代替（将左字数最大节点内容放入此节点），删除该最大节点。
     * 4、对于查找路径上的每个节点，做以下处理：（X为当前节点，P为父节点，T为当前节点的兄弟节点，L为T的左孩子，R为T的右孩子）
     *  4.1、X有两个黑儿子, T也有两个黑儿子。 反转X,P,T颜色
     *  4.2、X有两个黑儿子，T的左儿子为红。 L,T,P进行双旋转（之字形旋转）， X, P变色
     *  4.3、X有两个黑儿子，T的右儿子为红。 R,T，P单旋转（一字形旋转）， X,P,T,R都变色
     *  4.4、如果X有红色节点的孩子，那么X必定不是叶节点，则不是我们需要删除的节点。所以直接向下一层查找
     *      （1）如果新X为红，继续向下
     *      （2）如果新X为黑，（必有T为红，P为黑），单旋转T,P，同时，T,P变色，使得X的父节点为红色，然后回到第4步，处理当前节点。
     *
     * @param element
     */
    public void remove(T element){

    }

    /**
     * 如果一个节点有两个红色的子节点，则对其进行调整
     * @param element
     * @param current
     * @param parent
     * @param grand
     * @param great
     */
    private final void handleReorient(T element,RedBlackTreeNode<T> current, RedBlackTreeNode<T> parent, RedBlackTreeNode<T> grand, RedBlackTreeNode<T> great){
        //颜色调整
        current.color = RED;
        current.left.color = BLACK;
        current.right.color = BLACK;

        //如果父节点是红色节点，则和当前节点发生冲突
        if(parent.color == RED){
            //因为插入时是自上而下的处理，所以当父节点为红色时，父节点的兄弟节点必定为黑色，则可以进行自底向上插入中的第2步
            grand.color = RED;
            //旋转之后的子树的根为黑色
            if (compare(element, great) > 0){
                great.right = rotate(element, parent, grand);
                great.right.color = BLACK;
            }
            else{
                great.left = rotate(element, parent, grand);
                great.left.color = BLACK;
            }
        }
        header.right.color = BLACK;//保证根节点为黑色
    }

    /**
     * 根据当前节点、父节点、祖父节点是之字型还是一字型进行旋转
     * @param element
     * @param parent
     * @param grand
     * @return
     */
    private final RedBlackTreeNode<T> rotate(T element, RedBlackTreeNode<T> parent, RedBlackTreeNode<T> grand){
        if(compare(element, grand) > 0){
            if(compare(element, parent) > 0)
                return leftRotate(grand);
            else
                return rightAndLeftRotate(grand);
        }else {
            if (compare(element, parent) < 0)
                return rightRotate(grand);
            else
                return leftAndRightRotate(grand);
        }
    }

    private RedBlackTreeNode<T> rightRotate(RedBlackTreeNode<T> treeNode){
        RedBlackTreeNode<T> rotateNode = treeNode.left;
        treeNode.left = rotateNode.right;
        rotateNode.right = treeNode;
        return rotateNode;
    }

    private RedBlackTreeNode<T> leftAndRightRotate(RedBlackTreeNode<T> treeNode){
        treeNode.left = leftRotate(treeNode.left);
        return rightRotate(treeNode);
    }

    private RedBlackTreeNode<T> leftRotate(RedBlackTreeNode<T> treeNode){
        RedBlackTreeNode<T> rotateNode = treeNode.right;
        treeNode.right = rotateNode.left;
        rotateNode.left = treeNode;
        return rotateNode;
    }

    private RedBlackTreeNode<T> rightAndLeftRotate(RedBlackTreeNode<T> treeNode){
        treeNode.right = rightRotate(treeNode.right);
        return leftRotate(treeNode);
    }

    /**
     * 比较element和treeNode.element
     * @param element
     * @param treeNode
     * @return
     */
    private final int compare(T element, RedBlackTreeNode<T> treeNode){
        if (treeNode == header)
            return 1;
        else
            return element.compareTo(treeNode.element);
    }

    public RedBlackTreeNode<T> getRoot(){
        if (header.right != nullNode)
            return header.right;
        return null;
    }

    public void levelTraverse(Consumer<RedBlackTreeNode<T>> consumer){
        RedBlackTreeNode<T> node = getRoot();
        Queue<RedBlackTreeNode<T>> queue = new LinkedList<>();
        queue.offer(node);
        while (!queue.isEmpty()){
            RedBlackTreeNode<T> poll = queue.poll();
            consumer.accept(poll);
            if (poll.left != nullNode)
                queue.offer(poll.left);
            if(poll.right != nullNode)
                queue.offer(poll.right);
        }
    }

    public static class RedBlackTreeNode<T>{
        private T element;
        private RedBlackTreeNode<T> left;
        private RedBlackTreeNode<T> right;
        private int color;

        public RedBlackTreeNode(T element) {
            this(element, null, null);
        }

        public RedBlackTreeNode(T element, RedBlackTreeNode<T> left, RedBlackTreeNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.color = RedBlackTree.BLACK;
        }

        @Override
        public String toString() {
            return "RedBlackTreeNode{" +
                    "element=" + element +
                    ", color=" + color +
                    '}';
        }
    }


    public static void main(String[] args) {
        RedBlackTree<Integer> redBlackTree = new RedBlackTree<>();
        redBlackTree.insert(10);
        redBlackTree.insert(85);
        redBlackTree.insert(15);
        redBlackTree.insert(70);
        redBlackTree.insert(20);
        redBlackTree.insert(60);
        redBlackTree.insert(30);
        redBlackTree.insert(50);
        redBlackTree.insert(65);
        redBlackTree.insert(80);
        redBlackTree.insert(90);
        redBlackTree.insert(40);
        redBlackTree.insert(5);
        redBlackTree.insert(55);

        redBlackTree.levelTraverse(((RedBlackTree.RedBlackTreeNode<Integer> node)->{
            System.out.println(node);
        }));

        RedBlackTree.RedBlackTreeNode<Integer> node = redBlackTree.getRoot();
        System.out.println(node);
        System.out.println(node.left);
        System.out.println(node.right);
    }
}
