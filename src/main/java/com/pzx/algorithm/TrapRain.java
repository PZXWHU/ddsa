package com.pzx.algorithm;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 接雨水问题
 */
public class TrapRain {

    /**
     * 自己想到的算法：
     * 算法步骤：
     * 1.求出数组两端的初始peak（按顺序下一个元素小于当前元素，即可认为是peak）
     * 2.若两个初始peak位置重合，则返回0，当前地形处于凸出状态
     * 3.从左边的初始peak向右遍历（直到右边的peak），如果遇到height大于当前左peak，则计算当前左peak和当前位置之间的雨水量。并将当前左peak的位置移动到当前位置
     * 4.从右边初始peak向左遍历（直到左边的peak），如果遇到height大于当前右peak，则计算当前右peak和当前位置的雨水量，并将当前右peak的位置移动到当前位置。
     * 5.将计算的雨水量相加，返回结果
     *
     * @param height
     * @return
     */
    private static int trap(int[] height) {
        int length = height.length;
        int leftPeakIndex = -1;int rightPeakIndex = -1;

        for(int i = 0; i<length; i++){
            if(i == length-1 || (height[i+1] - height[i]) < 0){
                leftPeakIndex = i;
                break;
            }
        }
        for(int i = length -1 ;i>=0; i--){
            if(i == 0 || (height[i-1] - height[i]) < 0){
                rightPeakIndex = i;
                break;
            }
        }

        if(leftPeakIndex == rightPeakIndex)
            return 0;

        int result = 0;
        for(int i = leftPeakIndex + 1; i<= rightPeakIndex ; i++){
            if(height[i] >= height[leftPeakIndex]){
                for(int j = leftPeakIndex +1; j<i; j++){
                    result += height[leftPeakIndex] - height[j];
                }
                leftPeakIndex = i;
            }
        }

        for(int i = rightPeakIndex-1; i>=leftPeakIndex; i--){
            if(height[i] >= height[rightPeakIndex]){
                for(int j = rightPeakIndex -1; j > i; j--){
                    result += height[rightPeakIndex] - height[j];
                }
                rightPeakIndex = i;
            }
        }
        return result;

    }

    /**
     * 栈方法求解
     * 算法过程：
     * 1.使用栈来存储height数组的索引下标
     * 3.遍历height数组
     * 2.当栈为空或者栈顶元素所对应的height值大于当前height值时，将当前索引位置入栈
     * 3.当栈顶元素所对应的height值小于等于当前height值，栈顶元素出栈。取当前height和出栈后的栈顶元素对应的height的最小值，将其减去出栈元素所对应的height，再乘于当前位置到出栈后栈顶元素表示的索引位置的距离-1.
     * 4.执行第三步，将计算结果加到最后结果中，直到栈顶元素不再符合条件，将当前位置入栈。
     * 5.遍历完成，返回最终结果
     *
     * 算法解释：
     * 第三步求值的意义在于将当前位置到出栈之后的栈顶元素对应位置之间的位置进行填充，填充高度至当前位置和栈顶元素两者对应的较小值。
     * 重复第三步后，再将当前位置入栈，这表示从当前位置一直到栈顶元素（当前位置未入栈之前的栈顶元素）全部被填充至当前位置所对应的高度。
     * @param height
     * @return
     */
    private static int trap2(int[] height){

        int result = 0;
        Deque<Integer> stack = new LinkedList<>();
        for(int i=0; i<height.length; i++){
            if(stack.isEmpty() || height[stack.peek()] > height[i]){
                stack.push(i);
                continue;
            }
            while (!stack.isEmpty() && height[stack.peek()] <= height[i]){
                int stackTop = stack.pop();
                if(!stack.isEmpty())
                    result += (i - stack.peek()-1)*
                            (Math.min(height[i],height[stack.peek()]) - height[stackTop]);
            }
            stack.push(i);
        }
        return result;
    }

    /**
     * 双指针法
     * 算法步骤：
     * 一个位置填充多少水量，与当前位置的左边最大值和右边最大值之中的较小值有关
     * 1.如果当前左边最大值小于右边最大值，则从左边遍历
     * 2.若遍历元素若大于左边最大值，则更新最大值，否则，填充水量：当前左边最大值-当前位置。（当前左边最大值是当前位置的左边最大值，但是当前右边最大值不一定是当前位置的右边最大值。可是我们关注的是左右最大值中的较小值，所以只需要关注当前左边最大值即可）
     * 3.如果当前右边最大值小于左边最大值，则从右边遍历
     * 4.若遍历元素若大于右边最大值，则更新右边最大值，否则，填充水量。
     * 5.当遍历位置重合时，则结束遍历。
     * @param height
     * @return
     */
    private static int trap3(int[] height){
        int result = 0;
        int leftMax = 0; int rightMax = 0;
        int leftPosition = 0; int rightPosition = height.length-1;
        while (leftPosition <= rightPosition){
            if(leftMax < rightMax){
                if(height[leftPosition] < leftMax){
                    result += leftMax - height[leftPosition];
                }else {
                    leftMax = height[leftPosition];
                }
                leftPosition++;
            }else {
                if(height[rightPosition] < rightMax){
                    result += rightMax - height[rightPosition];
                }else {
                    rightMax = height[rightPosition];
                }
                rightPosition--;
            }
        }
        return result;
    }


}
