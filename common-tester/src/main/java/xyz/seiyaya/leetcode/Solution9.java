package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 */
public class Solution9 {

    public static void main(String[] args) {
        boolean palindrome = new Solution9().isPalindrome(10);
        System.out.println(palindrome);
    }

    public boolean isPalindrome(int x) {
        if(x < 0){
            return false;
        }
        List<Integer> list = new ArrayList<>();
        while(x != 0){
            list.add(x % 10);
            x = x / 10;
        }

        for(int i=0,j=list.size()-1; i < j ;i++ ,j--){
            if(!list.get(i).equals(list.get(j))){
                return false;
            }
        }
        return true;
    }
}
