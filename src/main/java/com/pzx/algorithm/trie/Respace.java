package com.pzx.algorithm.trie;

/**
 * https://leetcode-cn.com/problems/re-space-lcci/
 * 前缀树+dp
 */
public class Respace {
    public int respace(String[] dictionary, String sentence) {

        if(sentence.length() == 0) return 0;

        Trie root = new Trie();

        for(int i = 0 ; i < dictionary.length; i++){
            root.insert(dictionary[i]);
        }

        int[] dp = new int[sentence.length()];
        for(int i = 0; i < sentence.length(); i++){
            dp[i] = (i != 0 ? dp[i - 1] : 0) + 1;
            Trie trie = root;
            for(int j = i; j >= 0; j--){
                int index = sentence.charAt(j) - 'a';
                if(trie.next[index] == null)
                    break;
                trie = trie.next[index];
                if(trie.isEnd)
                    dp[i] = Math.min(dp[i], j == 0 ? 0 : dp[j  - 1]);
            }
        }

        return dp[sentence.length() - 1];

    }


    class Trie{
        Trie[] next;
        boolean isEnd;

        public Trie(){
            this.next = new Trie[26];

            this.isEnd = false;
        }

        public void insert(String s){
            int m = s.length();
            Trie trie = this;
            for(int i = m - 1; i >= 0; i--){
                int index = s.charAt(i) - 'a';

                if(trie.next[index] == null)
                    trie.next[index] = new Trie();
                trie = trie.next[index];

            }
            trie.isEnd = true;
        }

    }

}
