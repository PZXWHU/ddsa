package com.pzx.algorithm.dp;

/**
 * Given two words word1 and word2, find the minimum number of operations required to convert word1 to word2.
 *
 * You have the following 3 operations permitted on a word:
 *
 * 1、Insert a character
 * 2、Delete a character
 * 3、Replace a character
 *
 * Example 1:
 *
 * Input: word1 = "horse", word2 = "ros"
 * Output: 3
 * Explanation:
 * horse -> rorse (replace 'h' with 'r')
 * rorse -> rose (remove 'r')
 * rose -> ros (remove 'e')
 *
 */
public class EditDistance {

    /**
     * 将word1转换为word2的操作过程为：依次对word1的每个字母进行变换。则操作最后一步一定是是末尾增加一个字母，末尾删除一个字母、末尾字母替换为另一个字母或者不操作。
     * 上面假设最后一步操作发生在word1的末尾（整个操作序列的顺序是不重要的，所以可将word1的末尾变化放到最后），
     *
     *
     * 假设word1的前i个字符转换到word2的前j个字符的最小步骤为D[i][j]，则假设D[i][j]转换过程中的最后一步存在四种可能：
     * 1、word1末尾增加一个字母，则此时word1前i个字符已经转换为word2的前j-1个字符，这最少需要D[i][j-1]步操作，所以总操作步数为D[i][j-1] + 1
     * 2、word1末尾删除一个字母，则此时word1前i-1个字符已经转换为word2的前j个字符，这最少需要D[i-1][j]步操作，所以总操作步数为D[i-1][j] + 1
     * 3、word1末尾字母替换成另一个字母，则此时word1前i-1个字符已经转换为word2的前j-1个字符，这最少需要D[i-1][j-1]步操作，所以总操作步数为D[i-1][j-1] + 1。
     *      特殊情况下，word1的第i个字符与word2的第j个字符相同，则不需要替换，则最后一步不操作，所以总操作步数为D[i-1][j-1] 。
     *
     * 所以如果word1的第i个字符与word2的第j个字符相同：
     * D[i][j] = min(D[i][j-1] , D[i-1][j] , D[i-1][j-1] - 1) + 1
     * 所以如果word1的第i个字符与word2的第j个字符不相同：
     * D[i][j] = min(D[i][j-1] , D[i-1][j] , D[i-1][j-1]) + 1
     *
     * @param word1
     * @param word2
     * @return
     */
    public int minDistance(String word1, String word2) {
        if(word1 == null || word1==null)
            return -1;
        int len1 = word1.length();
        int len2 = word2.length();
        if (len1 * len2 == 0)
            return len1 + len2;

        int[][] D = new int[len1 + 1][len2 + 1];

        for(int i = 0; i < len1 + 1; i++){
            D[i][0] = i;
        }
        for (int j = 0; j < len2 + 1; j++){
            D[0][j] = j;
        }

        for(int i = 1; i < len1 + 1; i++){
            for (int j = 1; j < len2 + 1; j++){
                if (word1.charAt(i - 1) == word2.charAt(j -1)){
                    D[i][j] = Math.min(D[i - 1][j], Math.min(D[i][j - 1], D[i - 1][j - 1] - 1)) + 1;
                }else
                    D[i][j] = Math.min(D[i - 1][j], Math.min(D[i][j - 1], D[i - 1][j - 1])) + 1;
            }
        }
        return D[len1][len2];
    }

}
