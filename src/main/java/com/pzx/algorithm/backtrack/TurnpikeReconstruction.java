package com.pzx.algorithm.backtrack;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.sun.org.glassfish.external.amx.AMX;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.StreamSupport;

/**
 * 收费公路重建
 */
public class TurnpikeReconstruction {

    /**
     * 这里使用Multiset是因为距离是有重复的，但是使用List的结构对于contains，add，remove的速度都很慢
     * @param distanceSet
     * @return
     */
    private static int[] turnpike(Multiset<Integer> distanceSet){
        if (distanceSet == null)
            return null;

        int turnpikeSize = (int)(Math.pow(2 * distanceSet.size() + 0.25, 0.5) + 0.5);
        if(turnpikeSize * (turnpikeSize -1) / 2 != distanceSet.size())
            return null;

        int[] turnpikeLocation = new int[turnpikeSize];
        turnpikeLocation[0] = 0;
        turnpikeLocation[turnpikeSize - 1] = deleteMax(distanceSet);
        turnpikeLocation[turnpikeSize - 2] = deleteMax(distanceSet);
        Integer interval = turnpikeLocation[turnpikeSize - 1] - turnpikeLocation[turnpikeSize - 2];

        if(distanceSet.contains(interval)){
            distanceSet.remove(interval);
            if (place(turnpikeLocation, distanceSet, 1, turnpikeSize - 3))
                return turnpikeLocation;

        }
        return null;
    }

    /**
     *回溯算法，首先尝试右边位置，如果可以，则继续向下尝试；如果失败，则消除尝试产生的影响，尝试左边位置。
     * 如果左右位置都不行，则尝试失败，返回false，否则返回true。
     * @param turnpikeLocation
     * @param distanceSet
     * @param leftNextLocation  下一步可能放置收费站的左边位置
     * @param rightNextLocation 下一步可能放置收费站的右边位置
     * @return
     */
    private static boolean place(int[] turnpikeLocation, Multiset<Integer> distanceSet, int leftNextLocation, int rightNextLocation){
        if (distanceSet.isEmpty())
            return true;

        boolean found = false;
        int distanceMax = findMax(distanceSet);
        int turnpikeSize = turnpikeLocation.length;
        List<Integer> deletedDistanceList = new ArrayList<>();

        boolean isRightNextLocationPossible = true;
        for(int i = 0; i < leftNextLocation; i++){
            if(!distanceSet.contains(distanceMax - turnpikeLocation[i])){
                isRightNextLocationPossible = false;
                distanceSet.addAll(deletedDistanceList);
                deletedDistanceList.clear();
                break;
            }
            distanceSet.remove(Integer.valueOf(distanceMax - turnpikeLocation[i]));
            deletedDistanceList.add(Integer.valueOf(distanceMax - turnpikeLocation[i]));
        }
        if (isRightNextLocationPossible){
            for(int i = turnpikeLocation.length - 1; i > rightNextLocation; i--){
                if(!distanceSet.contains(turnpikeLocation[i] - distanceMax)){
                    isRightNextLocationPossible = false;
                    distanceSet.addAll(deletedDistanceList);
                    deletedDistanceList.clear();
                    break;
                }
                distanceSet.remove(Integer.valueOf(turnpikeLocation[i] - distanceMax));
                deletedDistanceList.add(Integer.valueOf(turnpikeLocation[i] - distanceMax));
            }
        }
        if (isRightNextLocationPossible){
            turnpikeLocation[rightNextLocation] = distanceMax;

            found = place(turnpikeLocation, distanceSet, leftNextLocation, rightNextLocation - 1);

            //如果向下走不成功，则进行回溯去尝试另外一种选择，并将当前选择所造成的影响恢复
            if (!found){
                distanceSet.addAll(deletedDistanceList);
                deletedDistanceList.clear();
            }
        }

        if (!found){
            boolean isLeftNextLocationPossible = true;
            for(int i = 0; i < leftNextLocation; i++){
                if(!distanceSet.contains(turnpikeLocation[turnpikeSize - 1] - distanceMax - turnpikeLocation[i])){
                    isLeftNextLocationPossible = false;
                    distanceSet.addAll(deletedDistanceList);
                    deletedDistanceList.clear();
                    break;
                }
                distanceSet.remove(Integer.valueOf(turnpikeLocation[turnpikeSize - 1] - distanceMax - turnpikeLocation[i]));
                deletedDistanceList.add(Integer.valueOf(turnpikeLocation[turnpikeSize - 1] - distanceMax - turnpikeLocation[i]));
            }
            if (isLeftNextLocationPossible){
                for(int i = turnpikeLocation.length - 1; i > rightNextLocation; i--){
                    if(!distanceSet.contains(turnpikeLocation[i] - turnpikeLocation[turnpikeSize - 1] + distanceMax)){
                        isRightNextLocationPossible = false;
                        distanceSet.addAll(deletedDistanceList);
                        deletedDistanceList.clear();
                        break;
                    }
                    distanceSet.remove(Integer.valueOf(turnpikeLocation[i] - turnpikeLocation[turnpikeSize - 1] + distanceMax));
                    deletedDistanceList.add(Integer.valueOf(turnpikeLocation[i] - turnpikeLocation[turnpikeSize - 1] + distanceMax));
                }
            }
            if (isLeftNextLocationPossible){
                turnpikeLocation[leftNextLocation] = turnpikeLocation[turnpikeSize - 1] - distanceMax;

                found = place(turnpikeLocation, distanceSet, leftNextLocation + 1, rightNextLocation);
                //回溯
                if (!found){
                    //undo the deletion
                    distanceSet.addAll(deletedDistanceList);
                    deletedDistanceList.clear();
                }
            }

        }
        return found;


    }


    private static Integer deleteMax(Multiset<Integer> set){
        Integer max = findMax(set);
        set.remove(max);
        return max;
    }

    private static Integer findMax(Multiset<Integer> set){
        //ForkJoinPool
        /*
        class MaxTask extends RecursiveTask<Integer>{
            private Spliterator<Integer> spliterator;
            private long targetBatchSize;
            public MaxTask(Spliterator<Integer> spliterator, long targetBatchSize) {
                this.spliterator = spliterator;
                this.targetBatchSize = targetBatchSize;
            }

            @Override
            protected Integer compute() {
                Spliterator<Integer> anotherSpliterator = null;

                if(spliterator.estimateSize() > targetBatchSize && (anotherSpliterator = spliterator.trySplit()) != null){
                    MaxTask maxTask1 = new MaxTask(spliterator, targetBatchSize);
                    MaxTask maxTask2 = new MaxTask(anotherSpliterator, targetBatchSize);
                    maxTask1.fork();
                    maxTask2.fork();
                    return Math.max(maxTask1.join() , maxTask2.join());
                }else {
                    //因为Multiset的spliterator不能分割，所以代码有错误
                    return StreamSupport.stream(spliterator,false).max(Integer::compareTo).get();
                }
            }
        }
        long targetBatchSize = 4;
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        Integer result = forkJoinPool.invoke(new MaxTask(set.spliterator(), targetBatchSize));
         */

        if (set == null || set.size() == 0)
            return null;
        return StreamSupport.stream(set.spliterator(),true).max(Integer::compareTo).get();


    }



    public static void main(String[] args) {
        Multiset<Integer> set = HashMultiset.create(Arrays.asList(1,2,2,2,3,3,3,4,5,5,5,6,7,8,10));
        System.out.println(Arrays.toString(turnpike(set)));
    }


}
