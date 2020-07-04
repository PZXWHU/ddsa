package com.pzx.algorithm.tree;

/**
 * 二叉搜索树的俩各个元素被交换了，请求其恢复
 *
 * Input: [1,3,null,null,2]
 *
 *    1
 *   /
 *  3
 *   \
 *    2
 *
 * Output: [3,1,null,null,2]
 *
 *    3
 *   /
 *  1
 *   \
 *    2
 *
 *
 */
public class RecoverBinarySearchTree {


    TreeNode pre = new TreeNode(Integer.MIN_VALUE);
    TreeNode swap1;
    TreeNode swap2;
    boolean isFirstSwap = true;
    public void recoverTree(TreeNode root) {

        findSwapTreeNode(root);
        swap1.val = swap1.val ^ swap2.val;
        swap2.val = swap1.val ^ swap2.val;
        swap1.val = swap1.val ^ swap2.val;
    }

    public void findSwapTreeNode(TreeNode root){
        if(root == null)
            return;

        findSwapTreeNode(root.left);
        if(pre.val > root.val){
            if(isFirstSwap){
                swap1 = pre;
                swap2 = root;
                isFirstSwap = false;
            }else{
                swap2 = root;
            }

        }
        pre = root;
        findSwapTreeNode(root.right);
    }


    private class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }
  }
}
