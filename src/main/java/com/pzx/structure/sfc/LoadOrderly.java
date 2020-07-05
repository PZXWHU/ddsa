package com.pzx.structure.sfc;

import com.google.common.base.Preconditions;
import com.pzx.App;

import java.util.*;

public class LoadOrderly {

    private Map<String, Integer> map = new HashMap<>();
    private final int dimension; //空间维度
    private final int bits; //曲线阶数 同时也是点坐标的比特数
    private final int length; //Hilbert index的比特数
    private final int maxCoordinate; //点坐标的最大值
    private final long maxIndex; //Hilbert index的最大值


    public LoadOrderly(int dimension, int bits) {
        Preconditions.checkArgument( dimension * bits < 64);
        this.dimension = dimension;
        this.bits = bits;
        this.length = dimension * bits;
        this.maxCoordinate = (1 << bits) - 1;
        this.maxIndex = (1L << length) - 1;
    }

    public void insert(int[] point){
        Preconditions.checkArgument(point.length == dimension,"the input point must be " + dimension + "-dimension");
        insertPointToMap(point);
        executePossiblePoints(point);
        updateMemoryMetric();

    }

    /**
     * 以当前点为中心的邻域与当前区域的交集所含有的点数，就是这个点总公共会被使用的次数。
     * @param point
     */
    private void insertPointToMap(int[] point) {
        int intersectCellNum = 1;
        for(int i = 0; i < point.length; i++) {
            intersectCellNum *= Math.min(maxCoordinate, point[i] + 1) - Math.max( 0, point[i] - 1) + 1;
        }
        map.put(createPointKey(point), intersectCellNum);

    }

    /**
     * 插入足够大的数字，保证不会移出内存
     * @param point
     */
    private void insertPointToMap(int[] point, int numberOfUse) {
        map.put(createPointKey(point), numberOfUse);
    }

    /**
     * 只有当前插入的点的邻接的点才有可能被执行，因为只有以邻接点为中心的邻域可能包含新插入的点
     * @param point
     * @return
     */
    private void executePossiblePoints(int[] point){

        int[] bias = new int[]{-1,0,1};
        int [] neighborPoint = new int[dimension];
        //对邻域中点依次进行检查
        for(int i = 0; i < Math.pow(3,dimension); i++){
            int ternary = i;
            //除k取余法
            for(int j = 0; j < dimension ; j++){
                neighborPoint[j] = point[j] + bias[ ternary % 3];
                ternary = ternary / 3;

            }
            if(isExecutable(neighborPoint)){
                execute(neighborPoint);
            }

        }

    }

    /**
     * 判断以当前点为中心及其邻域是否都已经加入内存
     * @param point
     */
    private boolean isExecutable(int[] point){
        int[] bias = new int[]{-1,0,1};
        int [] neighborPoint = new int[dimension];
        for(int i = 0; i < Math.pow(3,dimension); i++){
            int ternary = i;
            //除k取余法
            for(int j = 0; j < dimension ; j++){
                neighborPoint[j] = point[j] + bias[ ternary % 3];
                ternary = ternary / 3;
            }

            if(!map.containsKey(createPointKey(neighborPoint))){
                return false;
            }
        }
        return true;
    }

    /**
     * 将点和其邻接的点的使用次数减1，当使用次数为0时，则将点移出内存
     * @param point
     */
    private void execute(int[] point){

        int[] bias = new int[]{-1,0,1};
        int [] neighborPoint = new int[dimension];
        for(int i = 0; i < Math.pow(3,dimension); i++){
            int ternary = i;
            //除k取余法
            for(int j = 0; j < dimension ; j++){
                neighborPoint[j] = point[j] + bias[ ternary % 3];
                ternary = ternary / 3;
            }
            map.computeIfPresent(createPointKey(neighborPoint), (String key, Integer value) -> {
                if(value - 1 == 0)
                    return null;
                else
                    return value - 1;
            });
        }
    }

    private String createPointKey(int ... point){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < point.length; i++){
            stringBuilder.append(point[i]);
            if(i != point.length - 1)
                stringBuilder.append("_");
        }
        return stringBuilder.toString();
    }

    private int maxMemoryConsume = 0;
    private int insertCount = 0;
    private double averageMemoryConsume = 0;

    /**
     * 记录内存的使用情况
     */
    private void updateMemoryMetric(){
        maxMemoryConsume = Math.max(maxMemoryConsume, map.size());
        insertCount++;
        averageMemoryConsume = (averageMemoryConsume * (insertCount - 1) + map.size())/insertCount;
    }


    public static void main(String[] args) {
        int dimension = 2;
        int bits = 5;

        LoadOrderly loadOrderly = new LoadOrderly(dimension, bits);
        insertBoundaryPoint(loadOrderly);

        for(int i = 0; i < (1 << bits); i++) {
            for (int j = 0; j < (1 << bits); j++) {
                    loadOrderly.insert(new int[]{i, j});
            }
        }
        printLoadOrderly(loadOrderly,"顺序");


        /*
        -----------------------------------------------------------------------------------------
         */

        LoadOrderly loadOrderly1 = new LoadOrderly(dimension, bits);
        Hilbert hilbert = new Hilbert.Builder().dimension(dimension).bits(bits).build();

        insertBoundaryPoint(loadOrderly1);

        for(int i = 0; i<= loadOrderly1.maxIndex; i++){
            loadOrderly1.insert(hilbert.indexToPoint(i));
        }
        printLoadOrderly(loadOrderly1,"hilbert");

         /*
        -----------------------------------------------------------------------------------------
         */

        LoadOrderly loadOrderly2 = new LoadOrderly(dimension, bits);
        Zorder zorder = new Zorder.Builder().dimension(dimension).bits(bits).build();

        insertBoundaryPoint(loadOrderly2);
        for(int i = 0; i<= loadOrderly2.maxIndex; i++){
            loadOrderly2.insert(zorder.indexToPoint(i));
        }
        printLoadOrderly(loadOrderly2,"zorder");

         /*
        -----------------------------------------------------------------------------------------
         */

        LoadOrderly loadOrderly3 = new LoadOrderly(dimension, bits);
        insertBoundaryPoint(loadOrderly3);
        List<int[]> list = new ArrayList<>();
        for(int i = 0; i < (1 << bits); i++) {
            for (int j = 0; j < (1 << bits); j++) {
                list.add(new int[]{i, j});
            }
        }
        Collections.shuffle(list);
        for(int[] point : list){
            loadOrderly3.insert(point);
        }

        printLoadOrderly(loadOrderly3,"随机");


    }

    private static void printLoadOrderly(LoadOrderly loadOrderly, String loadMode){
        int bits = loadOrderly.bits;
        int dimension = loadOrderly.dimension;
        /*
        for (int m = (1 << bits); m >= -1; m--){
            for (int n = -1; n <= (1 << bits); n++){

                System.out.printf("%4d", loadOrderly.map.get(loadOrderly.createPointKey(n,m)));
            }
            System.out.println("");
        }

         */
        System.out.println("----------------------");
        System.out.println(loadMode +"：所使用的总点数："+ Math.pow((1 << bits) +2, dimension)
                + " 区域范围内的点数(除去缓冲区的点数)：" + Math.pow((1 << bits) , dimension)
                + "  内存中暂存的最大点数：" + loadOrderly.maxMemoryConsume
                + "  内存平均使用量：" + loadOrderly.averageMemoryConsume );
    }
    private static void insertBoundaryPoint(LoadOrderly loadOrderly){
        int bits = loadOrderly.bits;
        int dimension = loadOrderly.dimension;
        for(int i = -1; i <= (1 << bits); i++){
            for (int j = -1; j <= (1 << bits); j++){
                if (i == -1 || i == (1 << bits) || j == -1 || j == (1 << bits))
                    loadOrderly.insert(new int[]{i ,j });

            }
        }
    }


}
