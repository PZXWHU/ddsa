package com.pzx.structure.sfc;

import com.google.common.base.Preconditions;
import com.pzx.App;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LoadOrderly {

    private Map<String, Integer> interiorPointMap = new HashMap<>();
    private Map<String, Integer> exteriorPointMap = new HashMap<>();
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
    public void insertInteriorPoint(int[] point){
        Preconditions.checkArgument(point.length == dimension,"the input point must be " + dimension + "-dimension");
        insertPointToMap(point, interiorPointMap);
        executePossiblePoints(point);
        updateMemoryMetric();

    }

    /**
     * 插入区域周围的缓冲区中的点
     * @param point
     */
    public void insertExteriorPoint(int[] point) {
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
        }
        map.put(createPointKey(point), intersectCellNum);
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


    public static void main(String[] args) throws IOException {
        int dimension = 2;
        int bits = 2;
        FileWriter writer = new FileWriter("10阶曲线比较.txt");
        writer.write("加载方式,空间维度,曲线阶数,总点数,内存最大使用量,内存平均使用量,内存最大使用量占比,内存平均使用量占比" + "\n");

        LoadOrderly loadOrderly = new LoadOrderly(dimension, bits);
        insertBoundaryPoint(loadOrderly);

        for(int i = 0; i < (1 << bits); i++) {
            for (int j = 0; j < (1 << bits); j++) {
                loadOrderly.insertInteriorPoint(new int[]{j, i});
                printLoadOrderlyCurrentMemory(loadOrderly);
            }
        }
        printLoadOrderlyMemoryConsume(loadOrderly,"顺序");
        writeLoadOrderlyToFile(writer,loadOrderly,"顺序");

        /*
        -----------------------------------------------------------------------------------------
         */

        LoadOrderly loadOrderly1 = new LoadOrderly(dimension, bits);
        Hilbert hilbert = new Hilbert.Builder().dimension(dimension).bits(bits).build();

        insertBoundaryPoint(loadOrderly1);

        for(int i = 0; i<= loadOrderly1.maxIndex; i++){
            loadOrderly1.insertInteriorPoint(hilbert.indexToPoint(i));
        }
        printLoadOrderlyMemoryConsume(loadOrderly1,"hilbert");
        writeLoadOrderlyToFile(writer,loadOrderly1,"hilbert");

         /*
        -----------------------------------------------------------------------------------------
         */

        LoadOrderly loadOrderly2 = new LoadOrderly(dimension, bits);
        Zorder zorder = new Zorder.Builder().dimension(dimension).bits(bits).build();

        insertBoundaryPoint(loadOrderly2);
        for(int i = 0; i<= loadOrderly2.maxIndex; i++){
            loadOrderly2.insertInteriorPoint(zorder.indexToPoint(i));
        }
        printLoadOrderlyMemoryConsume(loadOrderly2,"zorder");
        writeLoadOrderlyToFile(writer,loadOrderly2,"zorder");

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
            loadOrderly3.insertInteriorPoint(point);
        }

        printLoadOrderlyMemoryConsume(loadOrderly3,"随机");
        writeLoadOrderlyToFile(writer,loadOrderly3,"随机");

         /*
        -----------------------------------------------------------------------------------------
         */

        LoadOrderly loadOrderly4 = new LoadOrderly(dimension, bits);
        insertBoundaryPoint(loadOrderly4);


        int k = 1 << bits * dimension;

        for(int i = 0; i <= 2 * (1 << bits) - 2; i++){
            for(int j = 0;j<= i;j++){
                if(j <= loadOrderly4.maxCoordinate && i - j <= loadOrderly4.maxCoordinate){
                    loadOrderly4.insertInteriorPoint(new int[]{j,i-j});

                }
            }
        }
        printLoadOrderlyMemoryConsume(loadOrderly4,"斜方向顺序");
        writeLoadOrderlyToFile(writer,loadOrderly4,"斜方向顺序");


        writer.close();
    }

    private static void printLoadOrderlyMemoryConsume(LoadOrderly loadOrderly, String loadMode){
        int bits = loadOrderly.bits;
        int dimension = loadOrderly.dimension;

        System.out.println("----------------------");
        long totalPointNum = (long)Math.pow((1 << bits) +2, dimension);

        System.out.println(loadMode);
        System.out.println("所使用的总点数："+ totalPointNum);
        System.out.println("区域范围内的点数(除去缓冲区的点数)：" +  (loadOrderly.maxIndex + 1));
        System.out.println("内存最大使用量：" + loadOrderly.maxMemoryConsume);
        System.out.println("内存最大使用量占区域内总点数的比值：" + loadOrderly.maxMemoryConsumeRatio);
        System.out.println("内存平均使用量：" + loadOrderly.averageMemoryConsume);
        System.out.println("内存平均使用量占区域内总点数的比值： " + loadOrderly.averageMemoryConsumeRatio);
    }

    private static void writeLoadOrderlyToFile(FileWriter writer,LoadOrderly loadOrderly,String loadMode) throws IOException{
        writer.write(loadMode + ","+ loadOrderly.dimension +","+loadOrderly.bits +
                ","+ (loadOrderly.maxIndex + 1)+","+ loadOrderly.maxMemoryConsume +","+loadOrderly.averageMemoryConsume
                + "," + loadOrderly.maxMemoryConsumeRatio+","+loadOrderly.averageMemoryConsumeRatio + "\n");
    }

    private static void printLoadOrderlyCurrentMemory(LoadOrderly loadOrderly){
        int bits = loadOrderly.bits;
        int dimension = loadOrderly.dimension;
        for (int m = (1 << bits) -1; m >= 0; m--){
            for (int n = 0; n <= (1 << bits) -1; n++){

                System.out.printf("%4d", loadOrderly.interiorPointMap.get(loadOrderly.createPointKey(n,m)));
            }
            System.out.println("");
        }
        System.out.println("----------------------");

    }

    private static void insertBoundaryPoint(LoadOrderly loadOrderly){
        int bits = loadOrderly.bits;
        int dimension = loadOrderly.dimension;
        for(int i = -1; i <= (1 << bits); i++){
            for (int j = -1; j <= (1 << bits); j++){
                if (i == -1 || i == (1 << bits) || j == -1 || j == (1 << bits))
                    loadOrderly.insertExteriorPoint(new int[]{i ,j });
            }
        }
    }


}
