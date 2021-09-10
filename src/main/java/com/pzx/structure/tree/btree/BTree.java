package com.pzx.structure.tree.btree;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 参考：https://blog.csdn.net/shenchaohao12321/article/details/83243314
 *
 * M阶B树定义：
 * 1、根结点孩子数量：[2，M]
 * 2、中间结点孩子数量：[ceil(M/2), M]
 * 3、根结点和中间结点的关键字数量：他们的孩子数量 -1
 * 4、一个结点当中：指向孩子指针和关键字的位置关系是：(指针1) 关键字A (指针2) 关键字B (指针3)
 *    每个关键字的值 > 左指针 指向的孩子树里结点中的关键字
 *    每个关键字的值 < 右指针 指向的孩子树里结点中的关键字
 * 5、叶子结点位于同一层
 * 6、结点中的关键字从小到大排列
 * 7、结点中关键字不重复
 *
 * M阶B+树定义：
 * 1、根结点孩子数量：[2，M]
 * 2、中间结点孩子数量：[ceil(M/2), M]
 * 3、根节点和中间结点的孩子数量 = 关键字数量
 * 4、根节点和中间结点的关键字 是 关键字对应子树中所有关键字的最大值，同时也存在于子树中
 * 5、根节点和中间结点只做索引，不包含数据
 * 6、叶子结点包含所有数据，并按照从小到大顺序排列
 * 7、叶子结点用指针连在一起
 * @param <K>
 */
public class BTree<K extends Comparable<? super K>, V> {


    /**
     * B树和B+树的插入删除特点
     *
     * 插入：
     * 如果插入超出关键字阈值，则将当前节点分裂为两个节点，在父节点中增加一个关键字，重复检查父节点。
     *
     * 删除操作：
     * 1.对于B树首先将删除的非叶子节点中的key替换成后继key（后继key一定在叶子节点中），然后删除后继key，类似于二叉搜索树删除。
     * 2.对于叶子节点删除，如果删除之后低于关键字阈值，判断兄弟节点关键字是否富余，是则借兄弟节点一个key，修改父节点key（B树是父结点key下移，兄弟结点key上移;B+树是直接移动到自己节点中，修改父节点key）
     * 3.如果兄弟节点不富余，则和兄弟节点合并，父节点key减少一个,重复检查父节点。
     */


    //B+树阶数
    private final int m;

    private BTreeNode<K, V> root;

    public BTree(int m) {
        this.m = m;
        this.root = new BTreeNode<>(m , true, null);
    }

    public void insert(K key, V value){
       root.insert(key, value);
       while (root.getParent() != null)
           root = root.getParent();
    }

    public void remove(K key){
       root.remove(key);
       while (root.getRouteTable().size() == 1){
           Object o = root.getRouteTable().firstEntry().getValue();
           if (o instanceof BTreeNode){
               root = (BTreeNode<K, V>) o;
           }else {
               break;
           }
       }
       root.setParent(null);

    }

    public V find(K key){
        if (root.getRouteTable().size() == 0) return null;

        BTreeNode<K, V> node = root;
        while (!node.isLeaf()){
            node = (BTreeNode<K, V>)node.getRouteTable().floorEntry(key).getValue();
        }
        return (V)node.getRouteTable().get(key);
    }

    public List<V> scan(K min, K max){
        if (root.getRouteTable().size() == 0) return Collections.EMPTY_LIST;

        BTreeNode<K, V> node = root;
        while (!node.isLeaf()){
            Map.Entry<K, Object> entry = node.getRouteTable().floorEntry(min);
            node = (BTreeNode<K, V>)(entry == null ? node.getRouteTable().firstEntry().getValue() : entry.getValue());
        }
        List<V> results = new ArrayList<>();
        while (node != null && node.getRouteTable().firstKey().compareTo(max) <= 0){
            for(Map.Entry<K, Object> entry : node.getRouteTable().subMap(min, true, max, true).entrySet()){
                results.add((V)entry.getValue());
            }
            node = node.getNext();
        }
        return results;
    }

    public void printTree(){
        BTreeNode<K, V> node = root;
        Queue<BTreeNode<K, V>> queue = new LinkedList<>();
        queue.offer(root);
        int size = 1;
        while (!queue.isEmpty()){
            int layerSize = 0;
            System.out.println("");
            for(int i = 0; i < size; i++){
                BTreeNode<K, V> poll = queue.poll();
                for(Map.Entry<K, Object> entry : poll.getRouteTable().entrySet()){
                    System.out.print(entry.getKey() + " ");
                    if (!poll.isLeaf()){
                        queue.offer((BTreeNode<K, V>) entry.getValue());
                        layerSize++;
                    }
                }

                System.out.print("   ");
            }
            size = layerSize;
        }
        System.out.println("");
    }


    public static void main(String[] args) {
        BTree<Integer, Integer> bTree = new BTree<>(6);
       /* bTree.insert(5,5);
        bTree.insert(10,10);
        bTree.insert(15,15);
        bTree.insert(20,20);
        bTree.insert(25,25);
        bTree.insert(26, 26);
        bTree.insert(30, 30);
        bTree.insert(31, 31);
        bTree.insert(32, 32);
        bTree.remove(26);
        bTree.printTree();
        System.out.println(bTree.scan(7, 22));*/

       // List<Integer> insertSet = Arrays.asList(19, 38, 7, 7, 58, 80, 6, 41, 77, 55, 56, 77, 85, 93, 84, 35, 85, 72, 29, 34, 11, 37, 33, 89, 28, 75, 34, 33, 3, 85, 10, 52, 43, 33, 41, 42, 78, 22, 26, 62, 73, 62, 45, 0, 17, 16, 2, 44, 44, 85, 34, 40);
        //List<Integer> insertSet = new ArrayList<>();
        TreeSet<Integer> insertSet = new TreeSet<>();
        try {
           Random random = ThreadLocalRandom.current();
           for(int i = 0; i < 100; i++){
               int d = random.nextInt(10000);
               insertSet.add(d);
               bTree.insert(d, d);
           }
           Iterator<Integer> iterator = insertSet.iterator();
           int i = 0;
           while (iterator.hasNext()){
               int next = iterator.next();
               if (i++ % 2 == 0){
                   bTree.remove(next);
                   iterator.remove();
               }

           }


           bTree.printTree();

           System.out.println(bTree.scan(770, 1003));
           System.out.println(insertSet.subSet(770,true, 1003, true));
       }catch (RuntimeException e){
           e.printStackTrace();
           bTree.printTree();
           //System.out.println(insertSet);
       }


    }

}
