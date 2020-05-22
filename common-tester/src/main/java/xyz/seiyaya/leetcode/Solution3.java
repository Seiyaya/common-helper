package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 * @author wangjia
 * @date 2020/5/22 11:30
 */
public class Solution3 {

    public static void main(String[] args) {
        int result = new Solution3().lengthOfLongestSubstring("pwwkew");
        System.out.println(result);

    }

    public int lengthOfLongestSubstring(String str) {
        char[] chars = str.toCharArray();
        int result = 0;
        int[] tmp = new int[200];
        Arrays.fill(tmp,0);
        for(int i=0,j=0;j<chars.length;j++){
            tmp[chars[j]]++;
            while(tmp[chars[j]] > 1){
                tmp[chars[i++]]--;
            }
            if(result < j-i+1){
                result = j-i+1;
            }
        }
        return result;
    }
}
