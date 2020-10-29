package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/22 9:28
 */
public class Solution763 {

    public static void main(String[] args) {
        List<Integer> result = new Solution763().partitionLabels("ababcbacadefegdehijhklij");
        System.out.println(result);
    }

    public List<Integer> partitionLabels(String S) {
        int[] last = new int[26];
        int length = S.length();
        for (int i = 0; i < length; i++) {
            last[S.charAt(i) - 'a'] = i;
        }
        List<Integer> partition = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < length; i++) {
            end = Math.max(end, last[S.charAt(i) - 'a']);
            if (i == end) {
                partition.add(end - start + 1);
                start = end + 1;
            }
        }
        return partition;
    }

}
