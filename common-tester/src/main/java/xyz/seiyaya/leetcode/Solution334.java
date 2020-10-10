package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/9 11:06
 */
public class Solution334 {

    public static void main(String[] args) {
        char[] chars = "hello".toCharArray();
        new Solution334().reverseString(chars);

        System.out.println(Arrays.toString(chars));
    }

    public void reverseString(char[] s) {
        if(s == null){
            return ;
        }
        int left = 0;
        int right = s.length-1;
        while(left < right){
            char tmp = s[left];
            s[left] = s[right];
            s[right] = tmp;
            left++;
            right--;
        }
    }
}
