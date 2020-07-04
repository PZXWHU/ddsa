package com.pzx.algorithm.tree;

import java.util.*;

/**
 * 二叉树非递归遍历
 */
public class BinaryTreeIterativelyTraversal {

    private  static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
        TreeNode(int x, TreeNode left, TreeNode right) {
            val = x;
            this.right = right;
            this.left = left;
        }
     }

    /**
     * 前序遍历
     * 算法流程：
     *  当前节点不为空或者栈不为空时，进行以下循环
     * 1.当前节点不为空，则访问该节点，并入栈。将当前节点赋值为当前节点的左孩子。
     * 2.当前节点为空，则进行出栈操作。将当前节点赋值为出栈元素的右孩子。
     * @param root
     * @return
     */
    private static List<Integer> preOrderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode node = root;

        while(node!=null||!stack.isEmpty()){
            while(node!=null){
                result.add(node.val);
                stack.push(node);
                node = node.left;
            }
            node = stack.pop().right;

        }
        return result;
    }

    /**
     * 中序遍历
     * 当前节点不为空或者栈不为空时，进行以下循环
     * 1.当前节点不为空，将其入栈，将当前节点赋值为当前节点的左孩子。
     * 2.当前节点为空，则进行出栈操作。访问出栈元素，并将当前节点赋值为出栈元素的右孩子。
     * @param root
     * @return
     */
    private List<Integer> inOrderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode node = root;
        while(node!=null || !stack.isEmpty()){
            if(node!= null){
                stack.push(node);
                node = node.left;
            }else{
                node = stack.pop();
                result.add(node.val);
                node = node.right;
            }
        }
        return result;
    }

    /**
     * 后序遍历
     * 算法流程：
     * 当前节点不为空或者栈不为空时，进行以下循环
     * 1.当前节点不为空，将其入栈。将当前节点赋值为当前节点的左孩子。
     * 2.当前节点不为空，查看栈顶元素。
     * 3.如果栈顶元素的右子树为空或者右子树是上一个访问的节点（说明当前循环的上一步是访问栈顶元素的右子树），则直接出栈。访问出栈元素，并将其赋值为上一个访问的节点。将当前节点赋值为null。
     * 4.如果栈顶元素的右子树不为空且右子树是不是上一个访问的节点（说明当前循环的上一步是访问栈顶元素的左子树），则将当前节点赋值为栈顶元素的右子树。
     *
     * 算法解析：
     * 对于前序遍历以及中序遍历，需要将父节点存入栈中，准备后续出栈访问（用于访问节点或者父节点的右子树），出栈操作肯定是在左子树处理之后再进行的。
     * 但是对于后序遍历，由于最后访问父节点。所以从左子树返回时，不能执行出栈操作。因为右子树还需要依靠栈访问父节点元素。所以栈顶元素操作需要判断上一步是从左子树返回的还是从右子树返回的。
     * 由于后序遍历父节点访问是在右子树之后，所以判断栈顶元素的右子树是否为上一个访问的节点，即可以知道上一步是否从右子树返回。
     * 如果是从右子树返回，则直接出栈，访问元素。将出栈元素赋值为上一个访问的节点。
     * 如果是从左子树返回，则不出栈，对栈顶元素的右子树进行处理。
     * @param root
     * @return
     */
    public List<Integer> postOrderTraversal(TreeNode root) {
        Deque<TreeNode> stack = new LinkedList<>();
        List<Integer> result = new ArrayList<>();
        TreeNode node = root,lastVisitNode = null;

        while(node!=null || !stack.isEmpty()){
            while(node!=null){
                stack.push(node);
                node = node.left;
            }
            node = stack.peek();
            if(node.right == null || node.right==lastVisitNode){
                //如果右子树为空或者刚刚访问过，则进行出栈，访问该节点，并将其赋值为上一个访问的节点。
                node = stack.pop();
                result.add(node.val);
                lastVisitNode = node;
                node = null;//下一循环直接跳到peek（）进行检查
            }else{
                //如果右子树不为空，且不是上一个访问的节点，则处理右子树。
                node = node.right;
            }

        }
        return result;

    }


    /**
     *-------------------------------------------------------------------------------------------
     * morris遍历的实现原则：
     * 来到当前节点，记为cur（引用）
     * 1、如果cur无左孩子，cur向右移动（cur = cur.right）
     * 2、如果cur有左孩子，找到cur左子树上最右的节点，记为mostright
     *     （1）如果mostright的right指针指向空，让其指向cur，cur向左移动（cur = cur.left）
     *     （2）如果mostright的right指针指向cur，让其指向空，cur向右移动（cur = cur.right）
     *
     * https://www.jianshu.com/p/959247c29a7b
     * https://blog.csdn.net/weixin_44137260/article/details/104426521
     *
     * 遍历时要记录访问过的父节点，因为需要回溯父节点去访问右子节点
     * 可以使用栈回溯，也可以使用父节点前驱节点的右指针记录访问过的父节点
     * 所以当访问一个节点时，如果是从栈中获取的或者左子树的前驱节点的右指针等于自己，说明之前已经访问过该节点了。
     * 由于后序遍历会访问一个节点三次（第一次访问，从左子树返回，从右子树返回），所以要使用lastprocessnode判断从哪个子树返回
     */

    private static List<Integer> preOrderTraversalMorris(TreeNode root) {
        if(root == null)
            return Collections.emptyList();

        TreeNode cur = root;
        TreeNode mostRight = null;
        List<Integer> resultList = new ArrayList<>();
        while (cur != null){
            if (cur.left == null){
                resultList.add(cur.val);
                cur = cur.right;
            }else {
                mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur){
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null){
                    mostRight.right = cur;
                    resultList.add(cur.val);
                    cur = cur.left;
                }else {
                    mostRight.right = null;  //只是保证不改变树结构
                    cur = cur.right;
                }
            }
        }
        return resultList;
    }

    private List<Integer> inOrderTraversalMorris(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        TreeNode cur = root;
        TreeNode mostRight = null;
        List<Integer> resultList = new ArrayList<>();
        while (cur != null) {
            if (cur.left == null) {
                resultList.add(cur.val);
                cur = cur.right;
            } else {
                mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    resultList.add(cur.val);
                    mostRight.right = null;  //只是保证不改变树结构
                    cur = cur.right;
                }
            }
        }
        return resultList;
    }

    /**
     * 当从左子树的最右节点返回时，左子树中的到达最右节点的路径上的节点们都还没被访问，所以以逆序的方式访问这些节点
     * @param root
     * @return
     */
    public List<Integer> postOrderTraversalMorris(TreeNode root) {
        if (root == null)
            return Collections.emptyList();

        TreeNode head = new TreeNode(0, root, null);
        TreeNode cur = head;
        TreeNode mostRight = null;
        List<Integer> resultList = new ArrayList<>();
        while (cur != null) {
            if (cur.left == null) {
                cur = cur.right;
            } else {
                mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    List<Integer> tmpList = new ArrayList<>();
                    mostRight.right = null;
                    TreeNode tmp = cur.left;
                    while (tmp != null){
                        tmpList.add(tmp.val);
                        tmp = tmp.right;
                    }
                    Collections.reverse(tmpList);
                    resultList.addAll(tmpList);
                    cur = cur.right;
                }
            }
        }
        return resultList;

    }


}
