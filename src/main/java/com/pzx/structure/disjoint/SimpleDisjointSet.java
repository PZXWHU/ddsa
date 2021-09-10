package com.pzx.structure.disjoint;

public class SimpleDisjointSet {

    int[] disjoint;

    public SimpleDisjointSet(int numElements){
        disjoint = new int[numElements];
        for (int i = 0; i < numElements; i++)
            disjoint[i] = -1;
    }

    public int find(int x){
        if(disjoint[x] < 0)
            return x;

        return disjoint[x] = find(disjoint[x]);
    }

    public void union(int x, int y){
        int index1 = find(x);
        int index2 = find(y);
        if(index1 == index2) return;
        disjoint[index2] = index1;

    }

}
