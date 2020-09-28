package com.pzx.algorithm.pointer;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * 有些数的素因子只有 3，5，7，请设计一个算法找出第 k 个数。注意，不是必须有这些素因子，而是必须不包含其他的素因子。例如，前几个数按顺序应该是 1，3，5，7，9，15，21。
 *
 * 输入: k = 5
 *
 * 输出: 9
 */
public class KthMagicNumber {

    /**
     * 三指针方法
     * 算法步骤：
     * magiNumber肯定是3^m * 5^n * 7^h，所以将magiNumber乘3、5、7都会得到另一个magiNumber
     * 但是我们需要第k个最小的magicNumber值，所以要用尽量小的值乘于3、5、7
     * 1.首先将1加入数组，1为最小的magicNumber
     * 2.设置三个指针，分别代表乘3、5、7
     * 3.将三个指针指向1，计算下一个最小的magicNumber，必定是三个指针所指的数分别乘3、5、、7中最小的数。
     * 4.将得到的下一个magicNumber所对应的指针向前移动一位。
     * 5.循环k次，即可得到结果。
     *
     * 算法解析：
     * 在指针前的数字是已经乘相应倍数，结果已经存入数组。而且数组是有序的。所以指针指向的数乘相应的倍数是目前可以得到最小的值。
     * @param k
     * @return
     */
    public int getKthMagicNumber(int k) {
        int p3 = 0; int p5 = 0; int p7 = 0;
        int[] magicNumber = new int[k];
        magicNumber[0] = 1;
        for(int i = 1; i<k; i++){
            magicNumber[i] = Math.min(magicNumber[p3]*3, Math.min(magicNumber[p5]*5, magicNumber[p7]*7));
            p3 = magicNumber[i] == magicNumber[p3]*3 ? p3+1 : p3;
            p5 = magicNumber[i] == magicNumber[p5]*5 ? p5+1 : p5;
            p7 = magicNumber[i] == magicNumber[p7]*7 ? p7+1 : p7;
        }
        return magicNumber[k-1];
    }

    /**
     * 小顶堆法
     * 算法思路：
     * 1.创建一个小顶堆，将1加入
     * 2.每次取堆顶元素，即最小的元素，乘3、5、7，分别将结果加入堆。注意重复元素！
     * 3.当第k次取出元素时，即获得结果。
     * @param k
     * @return
     */
    public int getKthMagicNumber1(int k) {
        Set<Long> magicNumberSet = new HashSet<>();
        Queue<Long>  magicNumberQueue = new PriorityQueue<>();
        magicNumberQueue.offer(1L);
        while (true){
            Long poll = magicNumberQueue.poll();
            if(!magicNumberSet.contains(poll)){
                magicNumberSet.add(poll);
                magicNumberQueue.offer(poll*3);
                magicNumberQueue.offer(poll*5);
                magicNumberQueue.offer(poll*7);
            }
            if(magicNumberSet.size() ==k)
                return poll.intValue();
        }

    }

}
