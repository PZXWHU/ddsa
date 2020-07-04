package com.pzx.structure.sfc;


import com.google.common.base.Preconditions;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.LongToIntFunction;

/**
 * Converts between Hilbert index and N-dimensional points
 *
 * reference:
 * Skilling, John. "Programming the Hilbert curve." AIP Conference Proceedings. Vol. 707. No. 1. AIP, 2004.
 * Lawder, Jonathan K., and Peter J. H. King. "Querying multi-dimensional data indexed using the Hilbert space-filling curve." ACM Sigmod Record 30.1 (2001): 19-24.
 * https://github.com/davidmoten/hilbert-curve
 * https://github.com/galtay/hilbertcurve
 * https://stackoverflow.com/questions/499166/mapping-n-dimensional-value-to-a-point-on-hilbert-curve
 */
public class Hilbert {

    private final int dimension; //空间维度
    private final int bits; //曲线阶数 同时也是点坐标的比特数
    private final int length; //Hilbert index的比特数
    private final long maxCoordinate; //点坐标的最大值
    private final long maxHilbertIndex; //Hilbert index的最大值


    public Hilbert(Builder builder){
        this.dimension = builder.dimension;
        this.bits = builder.bits;
        this.length = dimension * bits;
        this.maxCoordinate = (1l << bits) - 1;
        this.maxHilbertIndex = (1L << length) - 1;

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

        public Hilbert build(){
            Preconditions.checkArgument(dimension * bits < 64,
                    "the inputs dimension and bits is so large that the max Hilbert index is beyond Long.Max_VALUE!");
            return new Hilbert(this);
        }

    }

    /**
     * Converts a point to its Hilbert curve index.
     * @param point a array represent N-dimension point，in fact the coordinates have to be integer in current version
     * @return Hilbert Index
     */
    public long pointToHilbertIndex(long[] point) {
        Preconditions.checkArgument(point.length == dimension, "the input point must be " + dimension + "-dimension");
        for (long coordinate : point){
            Preconditions.checkArgument(coordinate <= maxCoordinate && coordinate >= 0, " the input point is out of range!");
        }
        long[] transposedIndex = pointToTransposedIndex(point);
        long hilbertIndex = transposedIndexToHilbertIndex(transposedIndex);
        return hilbertIndex ;
    }


    private long[] pointToTransposedIndex(long[] point) {
        final long M = 1L << (bits - 1);
        final int n = point.length; // n: Number of dimensions
        final long[] x = Arrays.copyOf(point, n);
        long p, q, t;
        int i;
        // Inverse undo
        for (q = M; q > 1; q >>= 1) {
            p = q - 1;
            for (i = 0; i < n; i++)
                if ((x[i] & q) != 0)
                    x[0] ^= p; // invert
                else {
                    t = (x[0] ^ x[i]) & p;
                    x[0] ^= t;
                    x[i] ^= t;
                }
        } // exchange
        // Gray encode
        for (i = 1; i < n; i++)
            x[i] ^= x[i - 1];
        t = 0;
        for (q = M; q > 1; q >>= 1)
            if ((x[n - 1] & q) != 0)
                t ^= q - 1;
        for (i = 0; i < n; i++)
            x[i] ^= t;

        return x;
    }

    private long transposedIndexToHilbertIndex(long[] transposedIndex) {
        long b = 0;
        int bIndex = length - 1;
        long mask = 1L << (bits - 1);
        for (int i = 0; i < bits; i++) {
            for (int j = 0; j < transposedIndex.length; j++) {
                if ((transposedIndex[j] & mask) != 0) {
                    b |= 1L << bIndex;
                }
                bIndex--;
            }
            mask >>= 1;
        }
        return b;
    }


    /**
     * Converts Hilbert curve index to ia point.
     * @param index Hilbert Index
     * @return a array represent N-dimension point，in fact the coordinates have to be integer in current version
     */
    public long[] hilbertIndexToPoint(long index){
        Preconditions.checkArgument(index <= maxHilbertIndex && index >= 0 ,"the input Hilbert Index is out of range!");
        long[] transposeIndex = hilbertIndexToTransposeIndex(index);
        long[] point = transposedIndexToPoint(transposeIndex);
        return point;
    }

    /**
     *Returns the transposed representation of the Hilbert curve index.
     *
     *  Example:
     *  input : 15-bit Hilbert integer = A B C D E F G H I J K L M N O  and the dimension is 3.
     *  output:
     *          X[0] = A D G J M                X[2]|
     *          X[1] = B E H K N    <------->       | /X[1]
     *          X[2] = C F I L O               axes |/
     *                 high  low                    0------ X[0]
     *
     * @param index
     * @return
     */
    private long[] hilbertIndexToTransposeIndex(long index) {
        long[] x = new long[dimension];
        for (int idx = 0; idx < 64; idx++) {
            if ((index & (1L << idx)) != 0) {
                int dim = (length - idx - 1) % dimension;
                int shift = (idx / dimension) % bits;
                x[dim] |= 1L << shift;
            }
        }
        return x;
    }

    private long[] transposedIndexToPoint(long[] x) {
        final long N = 2L << (bits - 1);
        // Note that x is mutated by this method (as a performance improvement
        // to avoid allocation)
        int n = x.length; // number of dimensions
        long p, q, t;
        int i;
        // Gray decode by H ^ (H/2)
        t = x[n - 1] >> 1;
        // Corrected error in Skilling's paper on the following line. The
        // appendix had i >= 0 leading to negative array index.
        for (i = n - 1; i > 0; i--)
            x[i] ^= x[i - 1];
        x[0] ^= t;
        // Undo excess work
        for (q = 2; q != N; q <<= 1) {
            p = q - 1;
            for (i = n - 1; i >= 0; i--)
                if ((x[i] & q) != 0L)
                    x[0] ^= p; // invert
                else {
                    t = (x[0] ^ x[i]) & p;
                    x[0] ^= t;
                    x[i] ^= t;
                }
        } // exchange
        return x;
    }




    public static void main(String[] args) {

        Hilbert hilbert = new Hilbert.Builder().dimension(3).bits(3).build();
        System.out.println(hilbert.pointToHilbertIndex(new long[]{7,4,6}));
        System.out.println(Arrays.toString(hilbert.hilbertIndexToPoint(34)));

    }

}
