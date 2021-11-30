package com.pzx.topic;

import java.util.Arrays;

public class Stock {

    /**
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/
     * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
     *
     * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
     *
     * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
     * @param prices
     * @return
     */
    public int maxProfit(int[] prices) {
        int min = prices[0];
        int n = prices.length;
        int res = 0;
        for(int i = 1; i < n; i++){
            if(prices[i] < min){
                min = prices[i];
            }else{
                res = Math.max(res, prices[i] - min);
            }
        }
        return res;

    }


    /**
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/
     * 给定一个数组 prices ，其中 prices[i] 是一支给定股票第 i 天的价格。
     *
     * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
     *
     * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
     * @param prices
     * @return
     */
    public int maxProfit1(int[] prices) {
        /*int ans = 0;
        int n = prices.length;
        for (int i = 1; i < n; ++i) {
            ans += Math.max(0, prices[i] - prices[i - 1]);
        }
        return ans;*/
        int n = prices.length;
        int own = -prices[0];
        int empty = 0;

        for(int i = 1; i < n; i++){
            own = Math.max(own, empty - prices[i]);
            empty = Math.max(empty, own + prices[i]);
        }

        return empty;
    }

    /**
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii/
     * 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
     *
     * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 两笔 交易。
     *
     * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
     * @param prices
     * @return
     */
    public int maxProfit2(int[] prices) {
        int n = prices.length;

        int buy1 = -prices[0];
        int sell1 = 0;
        int buy2 = -prices[0];
        int sell2 = 0;

        for(int i = 1; i < n; i++){
            buy1 = Math.max(buy1, -prices[i]);
            sell1 = Math.max(sell1, buy1 + prices[i]);
            buy2 = Math.max(buy2, sell1 - prices[i]);
            sell2 = Math.max(sell2, buy2 + prices[i]);
        }

        return sell2;
    }

    /**
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv/
     * 给定一个整数数组 prices ，它的第 i 个元素 prices[i] 是一支给定的股票在第 i 天的价格。
     *
     * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。
     *
     * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
     * @param k
     * @param prices
     * @return
     */
    public int maxProfit(int k, int[] prices) {
        int n = prices.length;

        if(n == 0) return 0;

        k = Math.min(k, n / 2);

        int[][] buy = new int[n][k + 1];//持有股票
        int[][] sell = new int[n][k + 1];//不持有股票

        for(int i = 0; i <= k; i++){
            buy[0][i] = -prices[0];
        }

        for(int i = 1; i < n; i++){
            buy[i][0] = Math.max(buy[i - 1][0], sell[i - 1][0] - prices[i]);
            for(int j = 1; j <= k; j++){
                buy[i][j] = Math.max(buy[i - 1][j], sell[i - 1][j] - prices[i]);
                sell[i][j] = Math.max(sell[i - 1][j], buy[i - 1][j - 1] + prices[i]);
            }
        }

        return Arrays.stream(sell[n - 1]).max().getAsInt();

    }


    /**
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/
     * 给定一个整数数组，其中第 i 个元素代表了第 i 天的股票价格 。​
     *
     * 设计一个算法计算出最大利润。在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:
     *
     * 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
     * 卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)。
     *
     * @param prices
     * @return
     */
    public int maxProfit4(int[] prices) {
        int n = prices.length;
        int buy = -prices[0];
        int frozen = 0;
        int sell = 0;

        for(int i = 1; i < n; i++){

            int newBuy = Math.max(buy, sell - prices[i]);
            int newFrozen = buy + prices[i];
            int newSell = Math.max(sell, frozen);
            buy = newBuy;
            frozen = newFrozen;
            sell = newSell;

        }

        return Math.max(frozen, sell);

    }

    /**
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
     * 给定一个整数数组 prices，其中第 i 个元素代表了第 i 天的股票价格 ；整数 fee 代表了交易股票的手续费用。
     *
     * 你可以无限次地完成交易，但是你每笔交易都需要付手续费。如果你已经购买了一个股票，在卖出它之前你就不能再继续购买股票了。
     *
     * 返回获得利润的最大值。
     *
     * 注意：这里的一笔交易指买入持有并卖出股票的整个过程，每笔交易你只需要为支付一次手续费。
     * @param prices
     * @param fee
     * @return
     */
    public int maxProfit(int[] prices, int fee) {
        int n = prices.length;

        int buy = -prices[0];
        int sell = 0;

        for(int i = 1; i < n; i++){
            buy = Math.max(buy, sell - prices[i]);
            sell = Math.max(sell, buy + prices[i] - fee);
        }

        return sell;

    }


}
