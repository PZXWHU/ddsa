package com.pzx.algorithm.backtrack;

import java.util.ArrayList;
import java.util.List;

public class BackTrack {

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

    public List<Integer> DFS(TreeNode root){
        List<Integer> list = new ArrayList<>();
        dfs(root, list);
        return list;

    }
    private void dfs(TreeNode root,List<Integer> list ) {
        if (root == null) return;


        //当回溯到叶子节点时，则判断是否符合条件,进行操作
        if (root.left == null && root.right == null) {
            list.add(root.val);
            //return;//如果状态改变 在此操作之前进行，则这里一定不能直接return，因为还需要运行后面的状态恢复操作。
        }

        //对每一个可能的分支（可以进行剪枝，避免不必要的探索），继续向下探索：

        //如果是回溯算法，则在这需要进行状态改变
        dfs(root.left, list);
        //如果是回溯算法，中间需要不需要状态恢复，然后再改变，需要根据实际情况判断
        dfs(root.right, list);
        //如果是回溯算法，则在这需要进行状态恢复
    }
}
