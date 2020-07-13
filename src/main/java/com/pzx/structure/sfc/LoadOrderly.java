package com.pzx.structure.sfc;

import com.google.common.base.Preconditions;
import com.pzx.App;
import com.sun.org.glassfish.external.amx.AMX;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LoadOrderly {

    private Map<String, Integer> interiorPointMap = new HashMap<>();
    private Map<String, Integer> exteriorPointMap = new HashMap<>();
    private int dimension; //空间维度
    private int bits; //曲线阶数 同时也是点坐标的比特数
    private int maxCoordinate; //点坐标的最大值
    private long maxIndex; //Hilbert index的最大值

    private LoadOrderly(){

    }
    /*
    public LoadOrderly(int dimension, int bits) {
        Preconditions.checkArgument( dimension * bits < 64,"the inputs dimension and bits is so large that the max Hilbert index is beyond Long.Max_VALUE!");
        this.dimension = dimension;
        this.bits = bits;
        this.maxCoordinate = (1 << bits) - 1;
        this.maxIndex = (1L << dimension * bits) - 1;
    }

     */

    public static LoadOrderly getInstanceFromCurveDegree(int dimension, int bits){
        Preconditions.checkArgument( dimension * bits < 64,"the inputs dimension and bits is so large that the max Hilbert index is beyond Long.Max_VALUE!");
        LoadOrderly loadOrderly = new LoadOrderly();
        loadOrderly.dimension = dimension;
        loadOrderly.bits = bits;
        loadOrderly.maxCoordinate = (1 << bits) - 1;
        loadOrderly.maxIndex = (1L << dimension * bits) - 1;
        loadOrderly.insertExteriorBoundaryPoint();
        return loadOrderly;
    }

    public static LoadOrderly getInstanceFromMaxCoordinate(int dimension, int maxCoordinate){
        Preconditions.checkArgument( Math.pow(maxCoordinate + 1, dimension) - 1 <= Long.MAX_VALUE,"the inputs dimension and maxCoordinate is so large that the max Hilbert index is beyond Long.Max_VALUE!");
        LoadOrderly loadOrderly = new LoadOrderly();
        loadOrderly.dimension = dimension;
        loadOrderly.bits = -1;//不用曲线阶数表示
        loadOrderly.maxCoordinate = maxCoordinate;
        loadOrderly.maxIndex = (long) Math.pow(maxCoordinate + 1, dimension) - 1;
        loadOrderly.insertExteriorBoundaryPoint();
        return loadOrderly;
    }

    /**
     * 插入区域边界外的点，便于判断区域边界的点是否可执行
     */
    private void insertExteriorBoundaryPoint(){
        int maxCoordinate = this.getMaxCoordinate();
        int dimension = this.getDimension();

        for(int i = 0; i<Math.pow(maxCoordinate + 3, dimension); i++){
            int index = i;
            int[] point = new int[dimension];
            boolean isExteriorBoundaryPoint = false;
            //除k取余法
            for(int j = 0; j<dimension; j++){
                point[j] = index % (maxCoordinate + 3) - 1;
                index = index / (maxCoordinate + 3);
                isExteriorBoundaryPoint = point[j] == -1 || point[j] == maxCoordinate + 1 ? true : isExteriorBoundaryPoint;
            }
            if (isExteriorBoundaryPoint){
                insertExteriorPoint(point);
            }

        }
        /*
        for(int i = -1; i <= maxCoordinate + 1; i++){
            for (int j = -1; j <= maxCoordinate + 1; j++){
                if (i == -1 || i == (maxCoordinate + 1) || j == -1 || j == (maxCoordinate + 1))
                    this.insertExteriorPoint(new int[]{i ,j });
            }
        }

         */
    }


    public void insertPoint(int[] point){
        Preconditions.checkArgument(point.length == dimension,"the input point must be " + dimension + "-dimension");
        for(int i = 0; i<dimension; i++){
            if(point[i] < 0 || point[i] > maxCoordinate){
                //insertExteriorPoint(point);
                throw new RuntimeException("the input point is out of range!");
            }
        }
        insertInteriorPoint(point);
    }

    /**
     * 插入区域内部的点
     *
     * 算法流程：
     * 1.计算当前点在此区域中需要被计算使用的次数（以这个点为中心的邻域与区域的交集所包含的点数即为当前点需要被使用的次数），将其记录在内存中。
     * 2.循环以当前点为中心的邻域中的所有点（包括自己），做以下操作：
     *      a)	判断自己是否可计算，即自己以及领域中的所有点是否已经在内存中。
     *      b)	若自己可计算，则将内存中记录的自己以及邻域中的点的需要被使用的次数减1。如果某点需要被使用的次数减小到0，则将其从内存中淘汰。
     *      c)	若自己不可计算，则跳过。
     *
     * @param point
     */
    private void insertInteriorPoint(int[] point){
        Preconditions.checkArgument(point.length == dimension,"the input point must be " + dimension + "-dimension");
        insertPointToMap(point, interiorPointMap);
        executePossiblePoints(point);
        updateMemoryMetric();

    }

    /**
     * 插入区域周围的缓冲区中的点
     * @param point
     */
    private void insertExteriorPoint(int[] point) {
        insertPointToMap(point, exteriorPointMap);
    }

    /**
     * 以当前点为中心的邻域与当前区域的交集所含有的点数，就是这个点总共会被使用的次数。
     * @param point
     * @param map
     */
    private void insertPointToMap(int[] point, Map<String, Integer> map) {
        int intersectCellNum = 1;
        for(int i = 0; i < point.length; i++) {
            intersectCellNum *= Math.min(maxCoordinate, point[i] + 1) - Math.max( 0, point[i] - 1) + 1;
            if (intersectCellNum <= 0)
                return;
        }
        map.put(createPointKey(point), intersectCellNum);
    }

    /**
     * 只有当前插入的点的邻接的点才有可能被执行，因为只有以邻接点为中心的邻域可能包含新插入的点
     * @param point
     * @return
     */
    private void executePossiblePoints(int[] point){

        int[] bias = new int[]{1,0,-1};
        int [] neighborPoint = new int[dimension];
        //对邻域中点依次进行检查
        for(int i = 0; i < Math.pow(3,dimension); i++){
            int ternary = i;
            //除k取余法
            //(将10进制整数转换为三进制，则可将三进制的每一位看作一个维度的坐标，则可将一个整数映射为八邻域（二维情况）中的一个点)
            //例如：7 = 2 * 3^1 + 1 * 3^0，所以可以映射为坐标（1，2）（八邻域坐标范围（0，0）->（2，2））
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
            String neighborPointKey = createPointKey(neighborPoint);
            if(!interiorPointMap.containsKey(neighborPointKey) && !exteriorPointMap.containsKey(neighborPointKey)){
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
            interiorPointMap.computeIfPresent(createPointKey(neighborPoint), (String key, Integer value) -> {
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

    /**
     * 内存使用记录
     */
    private int maxMemoryConsume = 0;
    private int insertCount = 0;
    private double averageMemoryConsume = 0;
    private double maxMemoryConsumeRatio = 0.0;
    private double averageMemoryConsumeRatio = 0.0;

    /**
     * 记录内存的使用情况
     */
    private void updateMemoryMetric(){
        maxMemoryConsume = Math.max(maxMemoryConsume, interiorPointMap.size());
        insertCount++;
        averageMemoryConsume = (averageMemoryConsume * (insertCount - 1) + interiorPointMap.size())/insertCount;
        maxMemoryConsumeRatio = (double) maxMemoryConsume / (maxIndex + 1);
        averageMemoryConsumeRatio = (double)averageMemoryConsume / (maxIndex + 1);
    }

    public int getDimension() {
        return dimension;
    }

    public int getBits() {
        return bits;
    }

    public int getMaxCoordinate() {
        return maxCoordinate;
    }

    public long getMaxIndex() {
        return maxIndex;
    }

    public int getMaxMemoryConsume() {
        return maxMemoryConsume;
    }

    public double getAverageMemoryConsume() {
        return averageMemoryConsume;
    }

    public double getMaxMemoryConsumeRatio() {
        return maxMemoryConsumeRatio;
    }

    public double getAverageMemoryConsumeRatio() {
        return averageMemoryConsumeRatio;
    }



    public void printLoadOrderlyCurrentMemory(){
        int maxCoordinate = this.getMaxCoordinate();
        int dimension = this.getDimension();
        for (int m = maxCoordinate; m >= 0; m--){
            for (int n = 0; n <= maxCoordinate; n++){

                System.out.printf("%4d", this.interiorPointMap.get(this.createPointKey(n,m)));
            }
            System.out.println("");
        }
        System.out.println("----------------------");

    }

    public void print3DLoadOrderlyCurrentMemory(){
        int maxCoordinate = this.getMaxCoordinate();
        int dimension = this.getDimension();
        for (int m = 0; m <= maxCoordinate; m++){
            for (int n = 0; n <= maxCoordinate; n++){
                for (int k = 0; k<=maxCoordinate; k++)

                System.out.printf("%4d", this.interiorPointMap.get(this.createPointKey(k,n,m)));
            }
            System.out.println("");
        }
        System.out.println("----------------------");

    }

}
