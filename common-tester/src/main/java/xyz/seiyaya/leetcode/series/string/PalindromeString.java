package xyz.seiyaya.leetcode.series.string;

import org.junit.Test;

/**
 * 判断字符串是是否是回文字符串
 * @author wangjia
 * @date 2020/5/21 10:39
 */
public class PalindromeString {

    /**
     * 从两边直接判断是否是回文
     */
    @Test
    public void testPalindromeDirect(){
        String str = "aba";
        int i = 0;
        int j = str.length()-1;
        while(i < j){
            if(str.charAt(i++) != str.charAt(j--)){
                System.out.println(str+"不是回文字符串");
                return ;
            }
        }
        System.out.println(str+"是回文字符串");
    }

    /**
     * 从中心扩展
     */
    @Test
    public void testPalindromeCenter(){
        String str = "abcba";
        int start = str.length()/2-1;
        int end = str.length()%2 == 0 ? str.length()/2 : str.length()/2+1;
        for(;start >= 0 && end<str.length() ;start--,end++){
            if (str.charAt(start) != str.charAt(end)) {
                System.out.println(str+"不是回文字符串");
                return ;
            }
        }
        System.out.println(str+"是回文字符串");
    }
}
