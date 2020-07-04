package com.pzx.structure.tree;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Trie又被称为前缀树、字典树
 *
 * 它的主要特点如下：
 * 根节点不包含字符，除根节点外的每一个节点都只包含一个字符。
 * 从根节点到某一节点，路径上经过的字符连接起来，为该节点对应的字符串。
 * 每个节点的所有子节点包含的字符都不相同。
 *
 *Trie的核心思想是空间换时间。利用字符串的公共前缀来降低查询时间的开销以达到提高效率的目的
 * 最大限度地减少无谓的字符串比较
 */
public class TrieTree {

    private TrieTreeNode root;

    public TrieTree() {
        this.root = new TrieTreeNode('\u0000');
    }

    /**
     * 字符串插入
     * 当节点没有需要的子节点时，则创建对应的子节点
     * 在字符串最后一个字符对应的子节点，将单词数量+1
     * @param str
     */
    private void insert(String str){
        if (str.length() == 0)
            return;
        TrieTreeNode node = root;
        for(char c: str.toCharArray()){
            if(!node.childMap.containsKey(c)){
                node.childMap.put(c, new TrieTreeNode(c));
            }
            node = node.childMap.get(c);
        }
        node.freqs++;

    }

    private int query(String str){
        if (str.length() == 0)
            return 0;
        TrieTreeNode node = root;
        for(char c: str.toCharArray()){
            if(!node.childMap.containsKey(c)){
                return 0;
            }
            node = node.childMap.get(c);
        }
        return node.freqs;
    }

    /**
     * 删除：记录字符串路径上的所有节点
     * 1、如果没有对应字符串（找不到对应路径或者最后一个节点freqs为0），则返回
     * 2.有对应字符串则将其节点的freqs--，进行回溯。对于路径上的每个节点，如果其子节点的freqs>0或者还有孩子，则不进行删除，并直接返回。否则删除对应子节点
     *
     *
     * 有一种更简单的删除方法，在节点中储存每个字符出现的次数
     * 在删除时，直接将字符频率减1，当字符频率为0时，直接删除
     * @param str
     */
    private void remove(String str){
        if (str.length() == 0)
            return;
        TrieTreeNode node = root;
        Deque<TrieTreeNode> stack = new LinkedList<>();
        for(char c: str.toCharArray()){
            if(!node.childMap.containsKey(c)){
                return ;
            }
            stack.push(node);
            node = node.childMap.get(c);
        }

        if (node.freqs == 0)//没有对应的字符串
            return;
        node.freqs--;

        TrieTreeNode pre = node;
        while (!stack.isEmpty()){
            node = stack.pop();
            if(pre.freqs > 0 || pre.childMap.size() > 0)
                return;
            node.childMap.remove(pre.ch);
        }
    }

    private static class TrieTreeNode{
        char ch;
        int freqs;  //记录单词出现次数
        Map<Character, TrieTreeNode> childMap;

        public TrieTreeNode(char ch) {
            this.freqs = 0;
            this.childMap = new HashMap<>();
        }
    }

    public static void main(String[] args) {
        String[] strings = new String[]{"asd","acv","dsa","dasq","cs","d","asd","dsa","dsa","fsdf","rew","fsdvd",
                "ewewq","fsdfs","daq","gfj","myuk","vnd","gd","tert","dav"};
        TrieTree tree = new TrieTree();
        Map<String,Integer> map = new HashMap<>();
        for(int i = 0; i<1000; i++){
            int randomIndex1 = ThreadLocalRandom.current().nextInt(strings.length);
            int randomIndex2 = ThreadLocalRandom.current().nextInt(strings.length);
            map.merge(strings[randomIndex1] + strings[randomIndex2], 1, (x,y)->x+y);
            tree.insert(strings[randomIndex1] + strings[randomIndex2]);
        }

        for(String str: map.keySet()){
            if(map.get(str) != tree.query(str)){
                System.out.println("error! " + str + "   "+ map.get(str)+ "  " + tree.query(str));
            }
        }

        for(String str: map.keySet()){
            int random = ThreadLocalRandom.current().nextInt(map.get(str));
            map.merge(str,random,(x,y)->x -y);
            for(int i = 0; i<random; i++)
                tree.remove(str);
            if(map.get(str) != tree.query(str)){
                System.out.println("error! " + str + "   "+ map.get(str)+ "  " + tree.query(str));
            }
        }


    }

}
