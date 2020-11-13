package com.pzx.algorithm.heap;

import java.util.*;

/**
 * 中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。
 *
 * 例如，
 *
 * [2,3,4] 的中位数是 3
 *
 * [2,3] 的中位数是 (2 + 3) / 2 = 2.5
 *
 * 设计一个支持以下两种操作的数据结构：
 *
 * void addNum(int num) - 从数据流中添加一个整数到数据结构中。
 * double findMedian() - 返回目前所有元素的中位数。

 */
public class MedianFinder {
    PriorityQueue<Integer> bigTopHeap;
    PriorityQueue<Integer> smallTopHeap;


    /** initialize your data structure here. */
    public MedianFinder() {
        bigTopHeap = new PriorityQueue<>((x,y)->Integer.compare(y, x));
        smallTopHeap = new PriorityQueue<>();

    }

    public void addNum(int num) {

        bigTopHeap.offer(num);
        smallTopHeap.offer(bigTopHeap.poll());

        if(smallTopHeap.size() > bigTopHeap.size()){
            bigTopHeap.offer(smallTopHeap.poll());
        }
    }

    public double findMedian() {
        if(bigTopHeap.size() == smallTopHeap.size()){
            return (bigTopHeap.peek() + smallTopHeap.peek()) / 2.0;
        }else
            return bigTopHeap.peek();
    }


    /**
     * 找出无序数组中的中位数
     * 更好的方法是使用堆，将N / 2 + 1个数建立大顶堆，依次比较之后的数，如果有数比堆顶小，则删除堆顶，将该数入堆。
     * 最后中位数为堆顶，或者连续的两个堆顶的平均值
     * @param nums
     * @return
     */
    public static double findMedianInUnsortedArray(int[] nums) {
        if(nums == null || nums.length == 0)
            return Integer.MIN_VALUE;

        int midLeft;int midRight;
        if(nums.length % 2 == 0){
            midLeft = nums.length / 2 - 1;
            midRight = nums.length / 2;
        }else {
            midLeft = midRight = nums.length / 2;
        }

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        stack.push(nums.length - 1);
        while (!stack.isEmpty()){
            int end = stack.pop();
            int start = stack.pop();
            int i = start; int j = end;
            int base = nums[i];
            while (i < j){
                while (i < j && base < nums[j]) j--;
                if(i < j) nums[i++] = nums[j];
                while (i < j && base > nums[i]) i++;
                if (i < j) nums[j--] = nums[i];
            }
            nums[j] = base;

            if(j > midRight){
                stack.push(start);
                stack.push(j - 1);
            }else if (j < midLeft){
                stack.push(j + 1);
                stack.push(end);
            }else if(j == midLeft && j == midRight){
                return nums[j];
            }else {
                if( j == midLeft){
                    int min = nums[end];
                    for(int k = j + 1; k < end; k++){
                        min = Math.min(min, nums[k]);
                    }
                    return (nums[j] + min) / 2.0;
                }else {
                    int max = nums[start];
                    for(int k = start + 1; k <= j - 1; k++){
                        max = Math.max(max, nums[k]);
                    }
                    return (nums[j] + max) / 2.0;
                }
            }
        }
        return Integer.MIN_VALUE;
    }


    /**
     * 找到两个有序数组中的中位数
     * 双指针法，时间复杂度O（N）
     * @param nums1
     * @param nums2
     * @return
     */
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {

        int p1 = -1;int p2 = -1;
        int mid1 = 0; int mid2 = 0;
        while(p1 + p2 + 2 < (nums1.length + nums2.length) / 2 + 1){
            mid1 = mid2;

            if(p1 == nums1.length - 1){
                p2++;
                mid2 = nums2[p2];
                continue;
            }

            if(p2 == nums2.length - 1){
                p1++;
                mid2 = nums1[p1];
                continue;
            }

            if(nums1[p1 + 1] > nums2[p2 + 1]){
                p2++;
                mid2 = nums2[p2];
            }else{
                p1++;
                mid2 = nums1[p1];
            }
        }
        if((nums1.length + nums2.length) % 2 == 0)
            return (mid1 + mid2) / 2.0;
        else
            return mid2;

    }

    /**
     * 二分查找法，时间复杂度Log（N）
     * https://www.cielyang.com/leetcode-4-%E5%AF%BB%E6%89%BE%E4%B8%A4%E4%B8%AA%E6%AD%A3%E5%BA%8F%E6%95%B0%E7%BB%84%E7%9A%84%E4%B8%AD%E4%BD%8D%E6%95%B0/
     * @param nums1
     * @param nums2
     * @return
     */
    public double findMedianSortedArrays1(int[] nums1, int[] nums2) {


        if((nums1.length + nums2.length) % 2 == 0){
            int index1 = (nums1.length + nums2.length) / 2;
            int index2 = index1 + 1;
            return (findKthNum(index1, nums1, nums2, 0, 0)
                    + findKthNum(index2, nums1, nums2, 0, 0)) / 2.0;
        }else
            return findKthNum((nums1.length + nums2.length) / 2 + 1, nums1, nums2, 0, 0);
    }

    public double findKthNum(int k, int[] nums1, int[] nums2, int start1, int start2){

        while(true){
            if(start1 > nums1.length - 1)
                return nums2[start2 + k - 1];
            if(start2 > nums2.length - 1)
                return nums1[start1 + k - 1];

            if(k == 1)
                return Math.min(nums1[start1], nums2[start2]);

            int mid1Index = Math.min(start1 +  k / 2 - 1, nums1.length - 1);
            int mid2Index = Math.min(start2 +  k / 2 - 1, nums2.length - 1);

            if(nums2[mid2Index] > nums1[mid1Index]){
                k -= (mid1Index - start1 + 1);
                start1 = mid1Index + 1;
            }else{
                k -= (mid2Index - start2 + 1);
                start2 = mid2Index + 1;

            }

        }


    }


    /**
     * 找出两个有序数组的中位数
     * 切分数组
     * @param nums1
     * @param nums2
     * @return
     */
    public static double findMedianSortedArrays2(int[] nums1, int[] nums2) {

        if(nums1.length > nums2.length){
            int[] tmp = nums2;
            nums2 = nums1;
            nums1 = tmp;
        }


        int start = 0;
        int end = nums1.length;
        int median1 = 0, median2 = 0;
        while(start <= end){

            int i = (start + end) / 2;
            int j = (nums1.length + nums2.length + 1) / 2 - i;

            int nums_im1 = (i == 0 ? Integer.MIN_VALUE : nums1[i - 1]);
            int nums_i = (i == nums1.length ? Integer.MAX_VALUE : nums1[i]);
            int nums_jm1 = (j == 0 ? Integer.MIN_VALUE : nums2[j - 1]);
            int nums_j = (j == nums2.length ? Integer.MAX_VALUE : nums2[j]);

            if(i == 0 || nums1[i - 1] <= nums2[j]){
                median1 = Math.max(nums_im1, nums_jm1);
                median2 = Math.min(nums_i, nums_j);
                start = i + 1;
            }else{
                end =  i - 1;
            }
        }


        if((nums2.length + nums1.length) % 2 == 0){
            return (median1 + median2) / 2.0;
        }else{
            return median1;
        }
    }


    public static void main(String[] args) {
        System.out.println(Arrays.toString(medianSlidingWindow(new int[]{1,2,3,4,2,3,1,4,2}, 3)));
    }

    static PriorityQueue<Integer> maxHeap = new PriorityQueue<>((x,y)->Integer.compare(y,x));
    static PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    static Map<Integer, Integer> deletedNum = new HashMap<>();


    /**
     * 中位数是有序序列最中间的那个数。如果序列的大小是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
     *
     * 例如：
     *
     * [2,3,4]，中位数是 3
     * [2,3]，中位数是 (2 + 3) / 2 = 2.5
     * 给你一个数组 nums，有一个大小为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。
     * 你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
     *
     *
     * @param nums
     * @param k
     * @return
     */
    public static double[] medianSlidingWindow(int[] nums, int k) {
        double[] median = new double[nums.length - k + 1];
        int left = 0;
        int right = k - 1;

        int maxHeapDeletedSize = 0, minHeapDeletedSize = 0;
        while(right < nums.length){
            if(left == 0){
                for(int i = 0; i < k; i++){
                    maxHeap.offer(nums[i]);
                    minHeap.offer(maxHeap.poll());
                    if(minHeap.size() > maxHeap.size() )
                        maxHeap.offer(minHeap.poll());
                }
            }else{
                deletedNum.put(nums[left - 1], deletedNum.getOrDefault(nums[left - 1], 0) + 1);
                if(nums[left - 1] <= maxHeap.peek())
                    maxHeapDeletedSize++;
                else
                    minHeapDeletedSize++;

                maxHeap.offer(nums[right]);
                minHeap.offer(maxHeap.poll());
                if(minHeap.size() - minHeapDeletedSize > maxHeap.size() - maxHeapDeletedSize)
                    maxHeap.offer(minHeap.poll());
            }

            while(deletedNum.containsKey(maxHeap.peek())){
                int deleted = maxHeap.poll();
                maxHeapDeletedSize--;
                if(deletedNum.get(deleted) == 1)
                    deletedNum.remove(deleted);
                else
                    deletedNum.put(deleted, deletedNum.get(deleted) - 1);
            }

            while(deletedNum.containsKey(minHeap.peek())){
                int deleted = minHeap.poll();
                minHeapDeletedSize--;
                if(deletedNum.get(deleted) == 1)
                    deletedNum.remove(deleted);
                else
                    deletedNum.put(deleted, deletedNum.get(deleted) - 1);
            }

            if(k % 2 == 0){
                median[left] = maxHeap.peek() * 0.5 + minHeap.peek() * 0.5;
            }else
                median[left] = maxHeap.peek();

            left++;
            right++;
        }
        return median;
    }




}
