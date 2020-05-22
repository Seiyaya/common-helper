package xyz.seiyaya.leetcode;

/**
 * 给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。
 * 空字符串也为有效的回文串
 * @author wangjia
 * @date 2020/5/22 16:17
 */
public class Solution125 {

    public static void main(String[] args) {
        boolean palindrome = new Solution125().isPalindrome("race a car");
        System.out.println(palindrome);
    }

    public boolean isPalindrome(String s) {
        String str = s.toLowerCase();
        char[] chars = str.toCharArray();
        for(int i=0,j=chars.length-1;i<j;){
            while(i < chars.length && !judgeInvalid(chars[i])){
                i++;
            }
            while(j >= 0 && !judgeInvalid(chars[j])){
                j--;
            }
            if(i<chars.length && j>=0 && chars[i] != chars[j] ){
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    @SuppressWarnings("all")
    private boolean judgeInvalid(char c) {
        if((c >= '0' && c<='9') || (c >='a' && c<='z')){
            return true;
        }
        return false;
    }
}
