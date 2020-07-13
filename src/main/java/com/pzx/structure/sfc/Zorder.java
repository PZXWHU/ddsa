package com.pzx.structure.sfc;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Zorder curve
 */
public class Zorder {

    private final int dimension; //空间维度
    private final int bits; //曲线阶数 同时也是点坐标的比特数

    private final int maxCoordinate; //点坐标的最大值
    private final long maxZorderIndex; //Hilbert index的最大值


    public Zorder(Builder builder){
        this.dimension = builder.dimension;
        this.bits = builder.bits;
        this.maxCoordinate = (1 << bits) - 1;
        this.maxZorderIndex = (1L << dimension * bits) - 1;

    }

    public static class Builder{
        private int dimension = 2; //空间维度
        private int bits = 1; //曲线阶数

        public Builder(){}

        public Builder dimension(int dimension){
            Preconditions.checkArgument(dimension > 1 ,"dimension must be greater than one!");
            this.dimension = dimension;
            return this;
        }

        public Builder bits(int bits){
            Preconditions.checkArgument(bits > 0 ,"bits must be greater than zero!");
            this.bits = bits;
            return this;
        }

        public Zorder build(){
            Preconditions.checkArgument(dimension * bits < 64,
                    "the inputs dimension and bits is so large that the max Zorder index is beyond Long.Max_VALUE!");
            return new Zorder(this);
        }

    }

    /**
     * Converts a point to its Zorder curve index.
     * @param point a array represent N-dimension point，in fact the coordinates have to be integer in current version
     * @return Zorder Index
     */
    public long pointToIndex(int[] point) {
        Preconditions.checkArgument(point.length == dimension, "the input point must be " + dimension + "-dimension");
        for (long coordinate : point){
            Preconditions.checkArgument(coordinate <= maxCoordinate && coordinate >= 0, " the input point is out of range!");
        }

        long index = 0L;
        for(int i = 0; i<bits; i++){
            for(int j = dimension - 1; j >= 0; j--){
                index =  index << 1 | ((point[j] >>>(bits -i-1)) & 1);
            }
        }
        return index;
    }

    /**
     * Converts Zorder curve index to a point.
     * @param index
     * @return
     */
    public int[] indexToPoint(long index){
        Preconditions.checkArgument(index <= maxZorderIndex && index >= 0 ,"the input Zorder Index is out of range!");
        int[] point = new int[dimension];
        for(int i = 0; i<bits; i++){
            for(int j = dimension - 1; j >= 0; j--){
                point[j] = (point[j] << 1) | (int)((index >>> ((bits - i - 1) * dimension + j)) & 1);
            }
        }
        return point;
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

    public long getMaxZorderIndex() {
        return maxZorderIndex;
    }
}
