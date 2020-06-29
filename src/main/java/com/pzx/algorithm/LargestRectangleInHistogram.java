package com.pzx.algorithm;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 直方图中的最大矩形
 * 输入[2,1,5,6,2,3]  输出10
 */
public class LargestRectangleInHistogram {

    /**
     * 单调栈方法
     *算法过程：
     * 1.创建边界数组，表示每一个高度的左右边界（左右边小于当前高度的最近位置）
     * 1.在栈中插入单元（-1，-1），分别表示高度和索引值
     * 2.遍历数组，如果高度值大于栈顶元素，则栈顶元素的索引值即为当前元素的左边界，将其记录在边界数组中。将当前高度值以及索引值入栈。
     * 3.如果高度值小于等于栈顶元素，循环进行出栈操作。出栈元素的右边界即为当前元素的索引值。当前元素的左边界，即为执行循环出栈之后的栈顶元素的索引值。
     * 4.遍历结束后，将栈中高度值不为-1的元素出栈，这些元素的右边界位置均为数组的长度。
     * 5.根据各个位置高度值以及其左右边界，求出各个位置的最大矩形。取最大值进行返回。
     *
     * 算法解析：
     * 对于第3步，高度值相同的栈顶元素出栈后，将右边界赋值为当前元素的位置，这个右边界是不准确的。
     * 但是对于具有相同高度的单元（相同高度的单元之间没有更低的单元），最后一个单元将获得准确的右边界，从而计算出正确数值。这个数值也是之前具有相同高度的单元的正确数值。所以并不影响最终结果。
     * @param heights
     * @return
     */
    private static int largestRectangleArea1(int[] heights) {

        int result = 0;
        Deque<int[]> stack = new LinkedList<>();//(height,index)
        stack.push(new int[]{-1,-1});
        int[][] bounds = new int[heights.length][2];//(leftBound,rightBound)

        for(int i=0; i<heights.length; i++){
            if(stack.peek()[0] < heights[i]){
                bounds[i][0] = stack.peek()[1];
                stack.push(new int[]{heights[i], i});
            }else{
                while(stack.peek()[0] >= heights[i]){
                    bounds[stack.pop()[1]][1] = i;
                }
                bounds[i][0] = stack.peek()[1];
                stack.push(new int[]{heights[i], i});
            }
        }
        while(!stack.isEmpty() && stack.peek()[0] != -1){
            bounds[stack.pop()[1]][1] = heights.length;
        }

        for(int i = 0; i<heights.length; i++){
            result = Math.max(result, heights[i]*(bounds[i][1] - bounds[i][0] -1));
        }
        return result;

    }

    /**
     * 简化的单调栈方法
     * @param heights
     * @return
     */
    private static int largestRectangleArea2(int[] heights) {
        Deque<Integer> stack = new LinkedList<>();
        stack.push(-1);
        int maxarea = 0;
        for (int i = 0; i < heights.length; ++i) {
            while (stack.peek() != -1 && heights[stack.peek()] >= heights[i])
                maxarea = Math.max(maxarea, heights[stack.pop()] * (i - stack.peek() - 1));
            stack.push(i);
        }
        while (stack.peek() != -1)
            maxarea = Math.max(maxarea, heights[stack.pop()] * (heights.length - stack.peek() -1));
        return maxarea;


    }


    /**
     * 动态规划方法
     * 1.设置二维数组maxRectangle[i][j]，其表示从i到j的最大矩形
     * 2.maxRectangle[i][j] = max（maxRectangle[i][j-1]，maxRectangle[i+1][j]，（j-i-1）*（i到j中间的最小高度））
     * 3.i=j，则等于自身高度
     * 4.由于求解maxRectangle[i][j] 比maxRectangle[i][j-1]和maxRectangle[i+1][j]的距离大1，所以先求解j-i等于1的所有结果，然后求j-i=2，3，4...，即正向推导，避免了某些结果的重复计算。
     * @param heights
     * @return
     */
    private static int largestRectangleArea3(int[] heights) {
        if(heights.length==0)
            return 0;

        int[][] maxRectangle = new int[heights.length][heights.length];
        for(int i=0; i<heights.length; i++){
            maxRectangle[i][i] = heights[i];
        }

        for(int i = 1; i< heights.length; i++){
            for(int j = 0; j<heights.length-i; j++){

                int minHeight = heights[j];
                for(int k = j; k <=j+i; k++){
                    minHeight = Math.min(minHeight,heights[k]);
                }
                maxRectangle[j][j+i] = Math.max(maxRectangle[j][j+i-1],maxRectangle[j+1][j+i]);
                maxRectangle[j][j+i] = Math.max(maxRectangle[j][j+i], (i+1)*minHeight);
            }
        }
        return maxRectangle[0][heights.length-1];
    }

    /**
     * 二维0、1矩阵求由1组成的最大矩形
     * Input:
     * [
     *   ["1","0","1","0","0"],
     *   ["1","0","1","1","1"],
     *   ["1","1","1","1","1"],
     *   ["1","0","0","1","0"]
     * ]
     *Output: 6
     *
     * 算法过程：
     * 1.构建一维数组，长度为矩阵的横向长度
     * 2.遍历二维矩阵的每一行，对行中的每一个元素，如果为0，将一维数组中的对应位置赋值为0，如果为1，则将一维数组中对应位置的数值+1.
     * 3.一行遍历结束之后，对一维数组调用上述求直方图中最大矩形的函数。
     * 4.比较每一行的结果，最大的即为所求结果。
     *
     * 算法解析：
     * 对于每一行，将此行及以上的行当作直方图，如果此行的某个元素为0，则该位置的直方图高度为0，否则在之前的高度上+1.即将二维问题转化为一维问题。
     *
     * @param matrix
     * @return
     */
    public int maximalRectangle(char[][] matrix) {
        if(matrix.length == 0)
            return 0;

        int max = 0;
        int[] height = new int[matrix[0].length];
        for(int i = 0;i<matrix.length;i++){
            for(int j = 0; j<height.length; j++){
                height[j] = matrix[i][j]=='0'?0:height[j]+1;
            }
            max = Math.max(max,largestRectangleArea2(height));
        }

        return max;
    }




    public static void main(String[] args) {
        int[] input = new int[1000];
        for(int j =0 ; j <10000; j++){
            for (int i = 0; i<1000; i++){
                input[i] = Math.abs(ThreadLocalRandom.current().nextInt(1000));
            }
            if(largestRectangleArea1(input)!=largestRectangleArea3(input)){
                System.out.println("false");
                break;
            }
        }



    }

}
