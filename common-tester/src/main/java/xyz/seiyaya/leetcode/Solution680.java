package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @date 2020/5/19 8:44
 */
public class Solution680 {

    public static void main(String[] args) {
        boolean abca = new Solution680().validPalindrome("eccer");
        System.out.println(abca);
    }

    public boolean validPalindrome(String s) {
        int i = 0, j = s.length() - 1;
        while(i < j){
            if(s.charAt(i) != s.charAt(j)){
                return isValid(s, i + 1, j) || isValid(s, i, j - 1);
            }
            i++;
            j--;
        }
        return true;
    }

    public boolean isValid(String s, int i, int j){
        while(i < j){
            if(s.charAt(i) != s.charAt(j)){
                return false;
            }
            i++;
            j--;
        }
        return true;
    }
}
