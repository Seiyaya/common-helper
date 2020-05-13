package xyz.seiyaya.leetcode;

import java.math.BigDecimal;

/**
 * 实现pow(x,n)
 *
 * @author wangjia
 * @date 2020/5/11 8:50
 */
public class Solution50 {

    public static void main(String[] args) {
        double v = new Solution50().myPow(2, -2);
        System.out.println(v);
        System.out.println(new BigDecimal(Double.MAX_VALUE));
    }

    public double myPow(double x, int n) {
        int start = n;
        if( n == 0){
            return 1;
        }
        double result = 1;
        while(n !=0){
            if( n % 2 != 0){
                result *= x;
            }
            x *=x;
            n = n/2;
        }
        return start < 0 ? 1/result : result;
    }

}
