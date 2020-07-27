package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/27 17:20
 */
public class Solution392 {

    public static void main(String[] args) {
        boolean subsequence = new Solution392().isSubsequence("b", "c");
        System.out.println("result:"+subsequence);
    }

    public boolean isSubsequence(String s, String t) {
        int i = 0;
        int count = 0;
        for(int j=0;i<s.length() && j<t.length();i++,j++){
            while(j<t.length()){
                if(s.charAt(i) != t.charAt(j)){
                   j++;
                }else{
                    count++;
                    break;
                }
            }
        }
        if( count != s.length()){
            return false;
        }
        return true;
    }
}
