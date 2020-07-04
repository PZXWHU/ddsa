package com.pzx.algorithm.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * 给定一个整数n，返回由1，2，3...n，组成的二叉搜索树
 * Input: 3
 * Output:
 * [
 *   [1,null,3,2],
 *   [3,2,null,1],
 *   [3,1,null,null,2],
 *   [2,1,3],
 *   [1,null,2,null,3]
 * ]
 *
 *
 */
public class UniqueBinarySearchTree {



    private static class TreeNode {
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

    public List<TreeNode> generateTrees(int n) {
        if(n == 0)
            return Collections.emptyList();
        return generateTrees(1,n);
    }

    public List<TreeNode> generateTrees(int start, int end){
        if(start > end)
            return Collections.singletonList(null);

        List<TreeNode> resultList = new ArrayList<>();
        for(int i = start; i<=end; i++){
            List<TreeNode> leftSubTree = generateTrees(start, i-1);
            List<TreeNode> rightSubTree = generateTrees(i+1, end);

            for(TreeNode leftSubTreeRoot : leftSubTree){
                for(TreeNode rightSubTreeRoot : rightSubTree){
                    resultList.add(new TreeNode(i, leftSubTreeRoot, rightSubTreeRoot));
                }
            }
        }
        return resultList;
    }





}
