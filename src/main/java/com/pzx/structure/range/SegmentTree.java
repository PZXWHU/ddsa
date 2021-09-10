package com.pzx.structure.range;

public class SegmentTree {

    int[] arr;
    int[] tree;
    int[] tag;
    int n;

    public SegmentTree(int[] arr){
        this.arr = arr;
        this.n = arr.length;
        this.tree = new int[4 * n];
        this.tag = new int[4 * n];
        buildTree(1, 1, n);
    }

    private void buildTree(int i, int left, int right){
        if(left == right){
            tree[i] = arr[left];
        }else{
            int mid = (left + right) >> 1;
            buildTree(leftChild(i), left, mid);
            buildTree(rightChild(i), mid + 1, right);
            pushUp(i);
        }
    }

    public void update(int uLeft, int uRight, int left, int right, int i, int k){
        if(uLeft <= left && uRight >= right){
            tree[i] += k * (right - left + 1);
            tag[i] += k;
        }else{
            pushDown(i, left, right);
            int mid = (left + right) >> 1;
            if(uLeft <= mid)
                update(uLeft, uRight, left, mid, leftChild(i), k);

            if(uRight > mid)
                update(uLeft, uRight, mid + 1, uRight, rightChild(i), k);

            pushUp(i);
        }
    }

    public int query(int qLeft, int qRight, int left, int right, int i){
        int res = 0;
        if(qLeft <= left && qRight >= right){
            return tree[i];
        }

        pushDown(i, left, right);
        int mid = (left + right) >> 1;

        if(qLeft <= mid)
            res += query(qLeft, qRight, left, mid, leftChild(i));

        if (qRight > mid)
            res += query(qLeft, qRight, mid + 1, right, rightChild(i));

        return res;

    }

    private void pushDown(int i, int left, int right){
        if (tag[i] > 0){
            int mid = (left + right) >> 1;

            tag[leftChild(i)] += tag[i];
            tree[leftChild(i)] += tag[i] * (mid - left + 1);

            tag[rightChild(i)] += tag[i];
            tree[rightChild(i)] += tag[i] * (right - mid);

            tag[i] = 0;
        }
    }

    private void pushUp(int i){
        tree[i] = tree[leftChild(i)] + tree[rightChild(i)];
    }

    private int leftChild(int p){
        return p << 1;
    }

    private int rightChild(int p) {
        return p << 1 + 1;
    }

}
