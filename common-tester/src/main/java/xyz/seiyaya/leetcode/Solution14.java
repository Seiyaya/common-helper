package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 */
public class Solution14 {

    public static void main(String[] args) {
        String s = new Solution14().longestCommonPrefix(new String[]{"dog","racecar","car"});
        System.out.println(s);
    }

    /**
     * 暴力穷举
     * @param strs
     * @return
     */
    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 1) {
            return strs[0];
        }
        StringBuilder result = new StringBuilder();
        if (strs.length == 0) {
            return "";
        }
        String start = strs[0];
        for (int i = 0; i < start.length(); i++) {
            for (int j = 1; j < strs.length; j++) {
                if (i >= strs[j].length() || start.charAt(i) != strs[j].charAt(i)) {
                    return result.toString();
                }
            }
            result.append(start.charAt(i));
        }
        return result.toString();
    }
}
