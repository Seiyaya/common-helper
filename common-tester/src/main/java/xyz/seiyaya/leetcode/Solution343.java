package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/30 17:58
 */
public class Solution343 {

    public static void main(String[] args) {
        int i = new Solution343().integerBreak(2);
        System.out.println("result:"+i);
    }

    public int integerBreak(int n) {
        if( n <=  3){
            return n-1;
        }
        int result = 1;
        while(n > 3){
            result *= 3;
            n -= 3;
        }
        return Math.max(result*n,result/3*(n+3));
    }
}
