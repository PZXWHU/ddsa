package com.pzx.algorithm.dp;

/**
 * 给定一个只包含 '(' 和 ')' 的字符串，找出最长的包含有效括号的子串的长度。
 */
public class LongestValidParentheses {

    public int longestValidParentheses(String s) {
        if(s == null || s.length() == 0)
            return 0;

        int[] L = new int[s.length()];
        L[0] = 0;
        for(int i = 1; i < s.length(); i++){
            if(s.charAt(i) == '(')
                L[i] = 0;
            else if(s.charAt(i - 1) == '(')
                L[i] = i > 1 ? L[i - 2] + 2 : 2;
            else{
                if(i - 1 - L[i - 1] >= 0 && s.charAt(i - 1 - L[i - 1]) == '('){
                    L[i] = L[i - 1] + 2 + (i - 2 - L[i - 1] >= 0 ? L[i - 2 - L[i - 1]] : 0);
                }else
                    L[i] = 0;
            }
        }

        int max = L[0];
        for(int l : L){
            max = Math.max(max, l);
        }
        return max;
    }

}
