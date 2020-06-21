package com.pzx.algorithm;

import java.util.*;

/**
 * 接雨水问题
 * 给定一维数组或者二维数组代表地面高度，求最大的积水体积
 * 例如：
 * 输入: [0,1,0,2,1,0,1,3,2,1,2,1]  输出 6
 * 输入：[ [1,4,3,1,3,2], [3,2,1,3,2,4], [2,3,3,2,3,1] ] 输出 4
 *
 * 解决思路：
 * 其实一维和二维的解决思路很相同。
 * 首先在边界构建围栏。（一维是两端点，二维是平面边界）
 * 其次，查找围栏中的最低点向内扩展，（一维则是找两端点的较低点，二维则是找围栏中的最低点）
 * 如果扩展单元大于当前单元，则将扩展单元作为新的围栏点。
 * 如果扩展单元的高度小于当前单元，则一定是可以积水的。因为当前单元是围栏中最低的点！！
 * 将扩展单元补充水量至当前单元同样的高度，并作为新的围栏点。
 * 因为一直向内扩展，所以检查过所有单元之后则获得最终结果。
 */
public class TrapRain {

    /**
     * 自己想到的算法：
     * 算法步骤：
     * 1.求出数组两端的初始peak（按顺序下一个元素小于当前元素，即可认为是peak）
     * 2.若两个初始peak位置重合，则返回0，当前地形处于凸出状态
     * 3.从左边的初始peak向右遍历（直到右边的peak），如果遇到height大于当前左peak，则计算当前左peak和当前位置之间的雨水量。并将当前左peak的位置移动到当前位置
     * 4.从右边初始peak向左遍历（直到左边的peak），如果遇到height大于当前右peak，则计算当前右peak和当前位置的雨水量，并将当前右peak的位置移动到当前位置。（此步和上一步有重复扫描的元素）
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

    /**
     * 二维平面接水问题
     * 算法过程：
     * 1.创建对应二维数组，标志某一单元是否已经检查过了
     * 2.创建优先队列，小顶堆
     * 3.将二维数据边界的单元全部入队列，以（x，y，height）为队列储存单元。并且将其对应的检查位设置为true。
     * 4.如果队列不为空，执行出队列操作。对获取的单元向上下左右扩展，如果不超出边界而且未检查过，则判断扩展单元的height是否小于当前单元，小于则将height差值加到结果中。
     * 5.最后将扩展单元的的检查位设置位true。
     * 6.当队列为空时，则输出结果。
     *
     * 算法解析：
     * 因为使用了优先队列，所以height值小的单元将首先出队列，进行扩展。
     * 当扩展单元的高度小于当前出队列单元时，扩展单元的周围必被高度值大于等于当前单元的单元所包围，所以可以直接为扩展单元补充水量：当前单元-扩展单元
     * 扩展单元高度小于当前单元时，其必然是可以积水的。假设其不能积水，则必有一条高度值均小于当前单元的流出边界的单元路径。但是边界上的元素一开始就入队列，高度值较小的将先出队列进行扩展。
     * 因为此条路径的高度值都小于当前单元，所以这些单元必将当前单元先出队列进行扩展。所以假设错误。
     * 参考：https://www.cnblogs.com/xym4869/p/12595864.html
     *
     * 算法思想：
     * 构造围栏积水，首先将平面边界作为围栏。
     * 找到围栏中的最低点，向内扩展，如果扩展单元高于当前单元，则删除当前单元，将扩展单元作为新的围栏点。这样围栏就逐渐的向内缩小。
     * 如果扩展单元高度小于当前单元，那么一定是可以积水的。因为当前单元是目前围栏中最低的单元。将扩展单元补充水量之后，作为新的围栏点。
     * 一直向内扩展，直至结束。
     * @param height
     * @return
     */
    private static int trap4(int[][] height){
        int n = height.length;
        int m = height[0].length;

        Queue<int[]> priorityQueue = new PriorityQueue<>((o1, o2)->Integer.compare(o1[2],o2[2]));
        boolean[][] isChecked = new boolean[n][m];

        for(int i = 0; i<n; i++){
            for(int j = 0; j<m; j++){
                if(i==0 || i==n-1 || j==0 || j==m-1){
                    priorityQueue.offer(new int[]{i, j, height[i][j]});
                    isChecked[i][j] = true;
                }
            }
        }

        int result = 0;
        int[] bias = new int[]{1,-1,0,0};
        while (!priorityQueue.isEmpty()){
            int[] poll = priorityQueue.poll();
            for(int i=0;i<4;i++){
                int newX = poll[0] + bias[i];
                int newY = poll[1] + bias[3-i];
                if(newX>=0 && newX<n && newY>=0 && newY<m && !isChecked[newX][newY]){
                    if(height[newX][newY] < poll[2]){
                        result += poll[2] - height[newX][newY];
                        priorityQueue.offer(new int[]{newX,newY,poll[2]});
                    }else {
                        priorityQueue.offer(new int[]{newX,newY,height[newX][newY]});
                    }
                    isChecked[newX][newY] = true;
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] height = new int[][]{{1,4,3,1,3,2},{3,2,1,3,2,4},{2,3,3,2,3,1}};
        System.out.println(trap4(height));
    }
}
