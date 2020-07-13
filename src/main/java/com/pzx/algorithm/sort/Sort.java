package com.pzx.algorithm.sort;


import java.util.*;

public class Sort {

    /**
     * 冒泡排序:时间复杂度O（N^2）
     * N-1趟排序，每趟排序相邻元素两两比较,顺序错误则进行交换，从而将第i大的元素向上交换（冒泡），到数组末端正确的位置。
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void bubbleSort(T[] a){
        for(int i = 0; i < a.length - 1; i++){
            for(int j = 0; j < a.length - i - 1; j ++){
                if(a[j].compareTo(a[j + 1]) > 0){
                    T tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                }
            }
        }
    }

    /**
     * 选择排序：时间复杂度O（N^2）
     * N-1趟遍历，每次遍历首先在未排序序列中找到最小（大）元素，存放到已排序序列的末尾。
     *
     * 和冒泡排序很像，但是选择排序是记录最小（大）的值，遍历之后进行一次交换。而冒泡排序是遍历之中不断的交换。
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void selectSort(T[] a){
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compareTo(a[min]) < 0) {
                    min = j;
                }
            }
            // 将找到的最小值和i位置所在的值进行交换
            if (i != min) {
                T tmp = a[i];
                a[i] = a[min];
                a[min] = tmp;
            }
        }
    }


    /**
     * 插入排序:时间复杂度O（N^2）
     * 1、由N-1趟排序组成，对于i=1到N-1趟排序，保证位置0到位置i上的元素为已排序状态
     * 2、每一趟排序，将位置i上的元素向前移动到正确的位置
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void insertSort(T[] a){
        for(int i = 1; i < a.length; i++) {
            T tmp = a[i];
            int j;
            for (j = i; j > 0 && tmp.compareTo(a[j - 1]) < 0; j--) {
                a[j] = a[j - 1];
            }
            a[j] = tmp;
        }
    }

    /**
     * 希尔排序使用一个增量序列[h1,h2,h3,h4....ht]，h1必须等于1
     * 对于增量序列中的每一个增量，对数组中的元素进行插入排序，保证a[i]<=a[i + h]
     *
     * 以下使用希尔增量进行排序，最坏情况O（N^2） ht = N / 2 , h1 = h2 / 2
     * Hibbard增量 ： h = 2^k - 1
     * @param a
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void shellSort(T[] a){
        for(int gap = a.length / 2; gap > 0; gap /= 2){
            //对于每一个gap进行一次插入排序。插入排序间隔为gap
            for(int i = gap; i < a.length; i++){
                T tmp = a[i];
                int j;
                for(j = i; j >= gap && tmp.compareTo(a[j - gap]) < 0; j -=gap){
                    a[i] = a[i - gap];
                }
                a[j] = tmp;
            }
        }

    }


    /**
     *
     * 基数排序 vs 计数排序 vs 桶排序
     *
     * 这三种排序算法都利用了桶的概念，但对桶的使用方法上有明显差异：
     * 基数排序：根据键值的每位数字来分配桶；
     * 计数排序：每个桶只存储单一键值；
     * 桶排序：每个桶存储一定范围的数值；
     */

    /**
     * 计数排序：时间复杂度O（N + M）
     * M为输入数据的最大值,输入数据必须是大于等于0的正整数
     * 计数排序的核心在于将输入的数据值转化为键存储在额外开辟的数组空间中。
     * 是一种特殊的桶排序，桶的个数为M
     * @param a
     * @param
     */
    public static void countingSort(int[] a){
        int maxValue = Arrays.stream(a).max().getAsInt();

        int[] bucket = new int[maxValue + 1];
        for (int value : a) {
            bucket[value]++;
        }

        int sortedIndex = 0;
        for (int j = 0; j < bucket.length; j++) {
            while (bucket[j] > 0) {
                a[sortedIndex++] = j;
                bucket[j]--;
            }
        }
    }

    /**
     * 桶排序，一般用于整数排序，时间复杂度不确定，和桶中使用的排序方式有关
     * 将所有数据划分到若干个桶中，对每个桶中的数据进行排序，然后拼接
     * 当输入的数据可以均匀的分配到每一个桶中，速度最快。当输入的数据被分配到了同一个桶中，速度最慢。
     *
     * @param a
     * @param bucketSize
     */
    public static void bucketSort(int[] a, int bucketSize){
        int minValue = a[0];
        int maxValue = a[0];
        for (int value : a) {
            if (value < minValue) {
                minValue = value;
            } else if (value > maxValue) {
                maxValue = value;
            }
        }

        int bucketCount = (int) Math.ceil((maxValue - minValue) / bucketSize);
        Map<Integer,List<Integer>> buckets = new TreeMap<>();

        // 利用映射函数将数据分配到各个桶中
        for (int i = 0; i < a.length; i++) {
            int index = (int) Math.floor((a[i] - minValue) / bucketSize);
            buckets.putIfAbsent(index,new ArrayList<>(bucketSize));
            buckets.get(index).add(a[i]);
        }

        int sortedIndex = 0;
        for (List<Integer> bucket : buckets.values()) {
            // 对每个桶进行排序，这里使用了插入排序
            Collections.sort(bucket);
            for (int value : bucket) {
                a[sortedIndex++] = value;
            }
        }

    }

    /**
     *时间复杂度：O（d(N+k)）d表示待排序列的最大位数，k表示每一位数的范围
     * 基数排序是一种非比较型整数排序算法，其原理是将整数按位数切割成不同的数字，然后按每个位数分别比较
     * 每次按照位数将所有数分发到不同的桶中，然后再依次取出，取出的数据是按照当前位数进行排序的数据。
     * 当循环执行完所有位数，即可得到完全排序的数据
     * 常用于字符串排序
     *
     * 以下代码要求输入数据大于等于0，对于含有负数的排序，可以进行正负数分离
     * @param a
     * @param
     */
    public static void radixSort(int[] a){
        //求最大值
        int maxValue = Arrays.stream(a).max().getAsInt();

        Map<Integer,List<Integer>> buckets = new TreeMap<>();
        //对数字的每一位都进行一次桶排序
        for(int i = 1; maxValue / i > 0; i *= 10){
            for(int j = 0; j < a.length; j++){
                int index = (a[j] / i) % 10;
                buckets.putIfAbsent(index, new ArrayList<>());
                buckets.get(index).add(a[j]);
            }
            int sortedIndex = 0;
            for(List<Integer> bucket : buckets.values()){
                for(int value : bucket){
                    a[sortedIndex++] = value;
                }
                bucket.clear();
            }
        }

    }

    /**
     * 时间复杂度：O（d(N+k)）d表示待排序列的最大位数，k表示每一位数的范围
     *计数基数排序，不需要使用ArrayList在桶中记录每个元素，只需要在桶中记录元素的个数
     * 然后从前向后将前一个桶的元素个数加到自己的桶的元素个数，这样桶中的数值表示：桶中最后一个元素在当前排序中的位置。
     * 按照桶中记录的位置，反向遍历原始数组，对每个元素查找其对应的桶，桶中记录的数值即是元素的位置。然后将对应桶的数值-1
     * @param a
     */
    public static void countingRadixSort(int[] a){
        //求最大值
        int maxValue = Arrays.stream(a).max().getAsInt();

        int[] output = new int[a.length];    // 存储"被排序数据"的临时数组

        //对数字的每一位都进行一次桶排序
        for(int i = 1; maxValue / i > 0; i *= 10){

            int[] buckets = new int[10];
            // 将数据出现的次数存储在buckets[]中
            for (int j = 0; j < a.length; j++)
                buckets[ (a[j] / i)%10]++;

            // 更改buckets[i]。目的是让更改后的buckets[i]的值，是该桶中最后一个数据在output[]中的位置。
            for (int j = 1; j < 10; j++)
                buckets[j] += buckets[j - 1];

            // 反向遍历原始数组，将数据存储到临时数组output[]中
            for (int j = a.length - 1; j >= 0; j--) {
                output[buckets[ (a[j] / i) % 10 ] - 1] = a[j];
                buckets[ (a[j] / i) % 10 ]--;
            }

            // 将排序好的数据赋值给a[]
            for (int j = 0; j < a.length; j++)
                a[j] = output[j];

        }

    }




    public static <T extends Comparable<? super T>> void checkSort(T[] a){
        if (a == null || a.length ==1)
            return;
        T pre = a[0];
        for(int i = 1; i<a.length; i++){
            if (a[i].compareTo(pre) < 0)
                throw new RuntimeException("排序不正确");
            pre = a[i];
        }

    }

    public static  void checkSort(int[] a){
        if (a == null || a.length ==1)
            return;
        int pre = a[0];
        for(int i = 1; i<a.length; i++){
            if (a[i] < pre)
                throw new RuntimeException("排序不正确");
            pre = a[i];
        }

    }


}
