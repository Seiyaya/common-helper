package xyz.seiyaya.leetcode;

/**
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
 * @author wangjia
 * @date 2020/5/21 8:46
 */
public class Solution5 {

    public static void main(String[] args) {
        String babad = new Solution5().longestPalindrome1("");
        System.out.println(babad);
    }

    /**
     * 中心扩展
     * @param s
     * @return
     */
    private String longestPalindrome1(String s) {
        if( s.length() < 2){
            return s;
        }
        char[] chars = s.toCharArray();
        int max = Integer.MIN_VALUE;
        int center = 0;
        for(int i=0;i<chars.length;i++){
            // 奇数情况以i为中心
            int begin = center(chars,i,i);
            // 偶数情况以i和i+1为中心
            int end = center(chars,i,i+1);
            if(max < Math.max(begin,end)){
                max = Math.max(begin,end);
                center = i;
            }
        }
        return s.substring(center - (max - 1) / 2, center + max / 2 + 1);
    }

    private int center(char[] chars, int start, int end) {
        while(start >= 0 && end<chars.length && chars[start] == chars[end]){
            start--;
            end++;
        }
        // 这里因为上面循环start已经是小于0的了，相当于start和end之间的距离多+2了，所以这里要-2
        return end - start + 1 -2;
    }

    /**
     * 直接枚举
     * @param s
     * @return
     */
    public String longestPalindrome(String s) {
        char[] chars = s.toCharArray();
        int result = Integer.MIN_VALUE;
        String str = "";
        for(int i=0;i<chars.length;i++){
            for(int j=i;j<chars.length;j++){
                if(isPalindrome(chars,i,j)){
                    if(j-i+1 > result){
                        result = j-i+1;
                        str = s.substring(i,j+1);
                    }
                }
            }
        }
        return str;
    }

    private boolean isPalindrome(char[] chars, int i, int j) {
        while(i < j){
            if(chars[i++] != chars[j--]){
                return false;
            }
        }
        return true;
    }
}
