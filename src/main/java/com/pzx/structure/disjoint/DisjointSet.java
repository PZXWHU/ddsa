package com.pzx.structure.disjoint;

/**
 * 并查集（不相交集）
 * 主要用于处理等价问题
 * 集合中根据等价关系将所有元素划分到若干个不相交的等价类
 * 并查集有两种操作：find和union
 * find一般返回等价类的名称，但是find实际上可以返回任何值，只要保证处于同一个等价类中的a，b，find(a)==find(b)为true即可，不是同一等价类中，则为false
 * union可以将含有a，b的两个等价类合并成一个新的等价类。
 *
 * 并查集分为：Quick Find和Quick Union，优化手段：基于size的Union、基于rank的Union、路径压缩优化
 * 以下实现为：Quick Union + 基于rank的Union + 路径压缩优化
 *
 * 假设开始所有的元素均已从0至N-1顺序编号（编号方法容易有散列方法实现），并且每个元素属于一个独立的等价类。
 * 本类使用森林表示并查集，树表示等价类，树节点只包含指向父节点的指针，根节点父指针为负数，表示树的高度的负值-1。
 * 使用数组储存森林，元素按照编号存储在数组对应位置，储存内容为父节点的指针。
 * find返回表示等价类的树的根节点（在数组中的位置），union将一棵树的根节点的父指针指向另一棵树的根节点。
 *
 * 并查集的时间复杂度：
 * 只带路径压缩或按秩合并的复杂度为均摊O(logN)
 * 同时使用两者的复杂度为均摊0（Alpha（N）），这里Alpha是Ackerman函数的某个反函数，在很大的范围内（人类目前观测到的宇宙范围估算有10的80次方个原子，
 * 这小于前面所说的范围）这个函数的值可以看成是不大于4的，所以0（Alpha（N））近似于O（1）。
 *
 * https://blog.csdn.net/johnny901114/article/details/80721436
 *
 */
public class DisjointSet {
    private int[] s;

    /**
     * -1表示根节点，且树的高度为0
     * @param numElements
     */
    public DisjointSet(int numElements){
        s = new int[numElements];
        for (int i = 0; i < numElements; i++)
            s[i] = -1;
    }

    /**
     * 返回根节点的位置，最坏时间复杂度0（logN）
     * @param x
     * @return
     */
    public int find(int x){
        if(s[x] < 0)
            return x;
        else
            return s[x] = find(s[x]);//路径压缩

    }

    /**
     * 按树的高度，合并包含两个元素的等价类，树的最大深度为logN
     * 时间复杂度O（logN）
     * （还可以按照树的大小进行合并操作）
     * @param x
     * @param y
     */
    public void union(int x, int y){
        int root1 = find(x);
        int root2 = find(y);
        if(s[root2] < s[root1])
            s[root1] = root2;
        else {
            if (s[root2] == s[root1]) //当两颗树的高度相同时，合并之后树的高度才会增加1
                s[root1]--;
            s[root2] = root1;
        }
    }

}
