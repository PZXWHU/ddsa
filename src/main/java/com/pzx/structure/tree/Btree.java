package com.pzx.structure.tree;

/**
 * 参考：https://blog.csdn.net/shenchaohao12321/article/details/83243314
 * @param <T>
 */
public class Btree<T extends Comparable<? super T>> {


    /**
     * B树和B+树的插入删除特点
     *
     * 插入：
     * 如果插入超出关键字阈值，则将当前节点分裂为两个节点，在父节点中增加一个关键字，重复检查父节点。
     *
     * 删除操作：
     * 1.对于B树首先将删除的非叶子节点中的key替换成后继key（后继key一定在叶子节点中），然后删除后继key，类似于二叉搜索树删除。
     * 2.对于叶子节点删除，如果删除之后低于关键字阈值，判断兄弟节点关键字是否富余，是则借兄弟节点一个key，修改父节点key（B树是父结点key下移，兄弟结点key上移，B+树是直接移动到自己节点中，修改父节点key）
     * 3.如果兄弟节点不富余，则和兄弟节点合并，父节点key减少一个,重复检查父节点。
     */

}
