package com.pzx;

import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.checkerframework.checker.units.qual.A;
import sun.nio.ch.DirectBuffer;
import sun.nio.ch.FileChannelImpl;
import sun.reflect.generics.tree.Tree;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{


    public static void main(String[] args) throws IOException {


    }

      public class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
     TreeNode(int x) { val = x; }
  }
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if(root == null)
            return "";

        StringJoiner sj = new StringJoiner(",");
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while(!queue.isEmpty()){
            TreeNode poll = queue.poll();
            if(poll != null){
                sj.add(poll.val + "");
                queue.offer(poll.left);
                queue.offer(poll.right);
            }
            else
                sj.add("null");
        }

        return sj.toString();

    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if("".equals(data))
            return null;

        String[] strings = data.split(",");
        TreeNode[] nodes = new TreeNode[strings.length];
        nodes[0] = new TreeNode(Integer.parseInt(strings[0]));
        int parentIndex = 0;
        for(int i = 1; i < strings.length; i+=2){
            while(nodes[parentIndex] == null)
                parentIndex++;
            if(!"null".equals(strings[i]))
                nodes[parentIndex].left = new TreeNode(Integer.parseInt(strings[i]));
            if(!"null".equals(strings[i + 1]))
                nodes[parentIndex].right = new TreeNode(Integer.parseInt(strings[i + 1]));
            nodes[i] = nodes[parentIndex].left;
            nodes[i + 1] = nodes[parentIndex].right;
            parentIndex++;
        }
        return nodes[0];

    }

}
