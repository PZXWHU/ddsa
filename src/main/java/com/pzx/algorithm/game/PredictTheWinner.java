package com.pzx.algorithm.game;

/**
 * https://leetcode-cn.com/problems/predict-the-winner/
 * https://leetcode-cn.com/problems/stone-game
 */
public class PredictTheWinner {

    public boolean PredictTheWinner(int[] nums) {
        int m = nums.length;
        int[][] dp = new int[m][m];
        for(int i = 0; i < m; i++){
            dp[i][i] = nums[i];
        }
        for(int i = 1; i <m; i++){
            for(int j = 0; j + i < m; j++){
                dp[j][j + i] = Math.max(nums[j] - dp[j + 1][j + i],
                        nums[j + i] - dp[j][j + i - 1]);
            }
        }

        return dp[0][m - 1] >= 0;
    }

}
