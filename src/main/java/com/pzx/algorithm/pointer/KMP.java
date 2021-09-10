package com.pzx.algorithm.pointer;

import java.util.Arrays;

public class KMP {


    public static int strStr(String haystack, String needle){
        //int[] next = getNext(needle);
        int[] maxMatch = getMaxPrePostfixMatch(needle);

        int i = 0, j = 0;
        while (i < haystack.length() && j < needle.length()){
            if (haystack.charAt(i) == needle.charAt(j)){
                i++;j++;
            }else {
                //i -= next[j];
                //i -= j == 0 ? -1 : maxMatch[j - 1];
                if (j == 0){
                    i++;j++;
                }else{
                    j = maxMatch[j - 1];
                }

            }
        }
        return j == needle.length() ? i - needle.length() : -1;
    }

    public static void main(String[] args) {
        String s1 = "abcdefg";
        String s2 = "def";

        System.out.println(strStr(s1, s2));
    }

    public static int[] getNext(String s){
        int[] next = new int[s.length()];
        char[] needle = s.toCharArray();
        int j = 0;
        next[0] = -1;

        for(int i = 1; i < s.length() - 1; i++){
            while (j > 0 && needle[i] != needle[j]){
                j = next[j];
            }
            if (needle[i] == needle[j])
                j++;

            next[i+1] = j;
        }
        return next;
    }

    public static int[] getMaxPrePostfixMatch(String s){
        int[] maxMatch = new int[s.length()];
        char[] needle = s.toCharArray();
        int j = 0;//表示当前位置上，前缀与后缀相同的最大长度
        maxMatch[0] = 0;

        for(int i = 1; i < s.length(); i++){
            while (j > 0 && needle[i] != needle[j]){
                j = maxMatch[j - 1];//快速找到前缀与后缀相同的长度
            }
            if (needle[i] == needle[j])
                j++;
            maxMatch[i] = j;
        }
        return maxMatch;
    }


}
