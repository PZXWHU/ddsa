package com.pzx.algorithm.sort;

import org.checkerframework.checker.units.qual.A;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;


/**
 * 快速排序 时间复杂度最坏O（N^2），平均O（NlogN）
 */
public class QuickSort {

    /**
     * 快排非递归写法
     * @param inputs
     * @param <T>
     */
    public static  <T extends Comparable<? super T>> void quickSortIteratively(T[] inputs){
        if (inputs == null || inputs.length == 0)
            return ;

        Deque<Integer> stack = new LinkedList<>();
        stack.push(0);stack.push(inputs.length - 1);
        while (!stack.isEmpty())
        {
            int end = stack.poll(); int start = stack.poll();
            T base = inputs[start];
            int i = start;int j = end;
            while (i < j){
                while (i < j && base.compareTo(inputs[j]) < 0) j--;
                if (i < j) inputs[i++] = inputs[j];
                while (i < j && base.compareTo(inputs[i]) > 0) i++;
                if(i < j) inputs[j--] = inputs[i];
            }
            //指针相遇处的元素肯定小于基准元素，进行交换
            inputs[j] = base;
            if(start < j-1){
                stack.push(start);
                stack.push(j-1);
            }
            if(j+1 < end){
                stack.push(j+1);
                stack.push(end);
            }
        }

    }

    /**
     * 快排递归写法
     * @param arr
     * @param <T>
     */
    public static <T extends Comparable<? super T>> void quickSort(T[] arr) {
        if (arr == null || arr.length == 0)
            return;
        quickSort(arr, 0, arr.length - 1);
    }

    /**
     * 指针交换法
     * @param inputs
     * @param start
     * @param end
     * @param <T>
     */
    private static <T extends Comparable<? super T>> void quickSort(T[] inputs ,int start, int end) {
        if (start >= end)
            return;

        //随即交换获得基准值，避免有序数组造成O（N^2）时间复杂度
        swap(inputs, start, (int)Math.random()*(end - start + 1) + start);

        T base = inputs[start];
        int i = start + 1;//必须为start + 1
        int j = end;
        /*
        while (true){
            while (j >= start && base.compareTo(inputs[j]) < 0) j--;
            while(i <= end && base.compareTo(inputs[i]) > 0) i++;
            if(i >= j) break;
            swap(inputs, i++, j--);
        }

         */
        while (i <= j){
            while (i <= j && base.compareTo(inputs[j]) < 0) j--;
            while(i <= j && base.compareTo(inputs[i]) > 0) i++;
            if(i <= j)
                swap(inputs, i++, j--);
        }

        swap(inputs,start, j);
        quickSort(inputs, start, j -1);
        quickSort(inputs,j+1, end);
    }

    /**
     * 挖坑填数法,无需交换操作
     *
     * @param inputs
     * @param start
     * @param end
     */
    private static <T extends Comparable<? super T>> void quickSort1(T[] inputs ,int start, int end){
        if (inputs.length == 0 || start >= end)
            return ;

        //随即交换获得基准值，避免有序数组造成O（N^2）时间复杂度
        swap(inputs, start, (int)Math.random()*(end - start + 1) + start);

        T base = inputs[start];
        int i = start;
        int j = end;

        while (i < j){
            while (i < j && base.compareTo(inputs[j]) < 0) j--;
            if(i < j) inputs[i++] = inputs[j];
            while (i < j && base.compareTo(inputs[i]) > 0) i++;
            if(i < j) inputs[j--] = inputs[i];
        }

        inputs[j] = base;
        quickSort1(inputs, start, i -1);
        quickSort1(inputs,i+1, end);
    }

    public void quickSort(int[] arr,int L,int R){
        if (arr.length == 0) return;
        int i = L;
        int j = R;
        int key = arr[(i + j)/2];
        while (i <= j){
            while (arr[i] < key)
                i++;
            while (arr[j] > key)
                j--;
            if (i <= j){
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        //上面一个while保证了第一趟排序支点的左边比支点小，支点的右边比支点大了。
        //“左边”再做排序，直到左边剩下一个数(递归出口)
        if (L < j)
            quickSort(arr,L,j);
        //“右边”再做排序，直到右边剩下一个数(递归出口)
        if(i < R)
            quickSort(arr,i,R);
    }


    /**
     * 三路快排，对于含有大量重复元素的数组运行速度较快
     * i指针指向较小元素的尾部+1
     * cur指针指向基准元素部分的尾部+1，也是当前元素
     * j指向较大元素头部-1
     * @param inputs
     * @param start
     * @param end
     * @param <T>
     */
    private static <T extends Comparable<? super T>> void quickSort2(T[] inputs ,int start, int end){
        if (inputs.length == 0 || start >= end)
            return ;

        //随即交换获得基准值，避免有序数组造成O（N^2）时间复杂度
        swap(inputs, start, (int)Math.random()*(end - start + 1) + start);

        T base = inputs[start];
        int i = start;
        int j = end;
        int cur = i;

        while (cur <= j) {
            if (inputs[cur].compareTo(base) == 0) {
                //当前元素等于基准元素，将cur指针+1
                cur++;
            } else if (inputs[cur].compareTo(base) < 0) {
                //当前元素小于基准元素，互换cur和i指向的元素（i指向的元素等于基准元素，i之前的元素全部小于基准元素）,再同时将cur和i指针+1
                swap(inputs, cur++, i++);
            } else {
                //当前元素大于基准元素，互换cur和j指向的元素（j之后的元素全部大于基准元素，j指向的元素并不确定，所以此时cur指针不移动）,再将j指针-1
                swap(inputs, cur, j--);
            }
        }
        quickSort2(inputs, start, i -1);
        quickSort2(inputs,j+1, end);

    }

    private static <T extends Comparable<? super T>> void swap(T[] inputs, int i, int j){
        T temp = inputs[i];
        inputs[i] = inputs[j];
        inputs[j] = temp;
    }

    public static void main(String[] args) {
        Integer[] array = new Integer[10000];
        int k = 1000;
        while ( k > 0){
            k--;

            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            for (int i = 0; i<10000; i++){
                array[i] = threadLocalRandom.nextInt(4000);
            }
            QuickSort.quickSort(array,0,array.length-1);
            Sort.checkSort(array);
        }

        k = 1000;
        while ( k > 0){
            k--;

            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            for (int i = 0; i<10000; i++){
                array[i] = threadLocalRandom.nextInt(4000);
            }
            QuickSort.quickSort1(array,0,array.length-1);
            Sort.checkSort(array);
        }

        k = 1000;
        while ( k > 0){
            k--;

            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            for (int i = 0; i<10000; i++){
                array[i] = threadLocalRandom.nextInt(4000);
            }
            QuickSort.quickSort2(array,0,array.length-1);
            Sort.checkSort(array);
        }

    }



}
