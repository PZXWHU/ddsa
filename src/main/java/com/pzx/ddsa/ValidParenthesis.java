package com.pzx.ddsa;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 有效的括号
 *
 */
public class ValidParenthesis {

    /**
     * 算法过程
     * 1.遍历字符串
     * 2.遇到左括号则入栈
     * 3.遇到右括号则检查栈顶是否为对应的左括号，是则出栈，否则返回false
     * 4.当遍历完成后，如果栈中还有元素，则返回false，否则返回true
     * @param s
     * @return
     */
    public boolean isValid(String s) {
        Map<Character, Character> bracket = new HashMap<>();
        bracket.put('(',')');bracket.put('[',']');bracket.put('{','}');
        Deque<Character> stack = new LinkedList<>();

        for(char c : s.toCharArray()){
            if(bracket.containsKey(c)){
                stack.push(c);
            }else{
                if(stack.isEmpty())
                    return false;

                if(c == bracket.get(stack.peek()))
                    stack.pop();
                else
                    return false;
            }
        }

        return stack.isEmpty();

    }
}
