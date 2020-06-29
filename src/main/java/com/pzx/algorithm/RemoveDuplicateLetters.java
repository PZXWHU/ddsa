package com.pzx.algorithm;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 去掉字符串中的重复字符，保持字符相对位置不变且字典序最小
 * Input: "cbacdcbc"  Output: "acdb"
 */
public class RemoveDuplicateLetters {

    /**
     *栈解决
     *
     * 算法步骤：
     * 1.遍历字符串，获取每一个字符的重复数量
     * 2.遍历字符，每遍历一个，就将该字符的重复次数减1
     * 3.当栈中已经包含当前字符，则直接略过。
     * 3.当栈不为空，且栈顶元素大于当前元素且栈顶元素还有重复字符，则将栈顶出栈。重复进行直到不符合条件
     * 4.将当前元素入栈。
     * 5.遍历所有元素之后，栈中元素即为结果。
     * @param s
     * @return
     */
    private static String removeDuplicateLetters(String s) {

        Map<Character, Integer> letterCount = new HashMap<>();
        Deque<Character> stack = new LinkedList<>();
        int letterInStack = 0;
        for(char c : s.toCharArray()){
            letterCount.put(c,letterCount.getOrDefault(c,0)+1);
        }

        for(char c: s.toCharArray()){
            letterCount.put(c, letterCount.get(c) -1);
            if(((letterInStack >> (c - 'a')) & 1) == 1){
                continue;
            }
            while(!stack.isEmpty() && stack.peek() > c && letterCount.get(stack.peek()) > 0){
                letterInStack &= ~(1 << (stack.pop() - 'a'));
            }
            stack.push(c);
            letterInStack |= 1 << (c - 'a');
        }

        StringBuilder sb = new StringBuilder();
        while(!stack.isEmpty()){
            sb.append(stack.pollLast());
        }
        return sb.toString();

    }

    public static void main(String[] args) {
        System.out.println(removeDuplicateLetters("acbac"));
    }
}
