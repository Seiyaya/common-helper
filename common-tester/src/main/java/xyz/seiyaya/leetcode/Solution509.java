package xyz.seiyaya.leetcode;

/**
 * 斐波那契数列
 * @author wangjia
 * @version v1.0
 * @date 2021/1/4 16:16
 */
public class Solution509 {

    public static void main(String[] args) {
        /**
         * F(0) = 0，F(1) = 1
         * F(n) = F(n - 1) + F(n - 2)，其中 n > 1
         */
        int fib = new Solution509().fib(3);
        System.out.println(fib);
    }


    public int fib(int n) {
        if(n == 0){
            return 0;
        }
        if(n == 1){
            return 1;
        }
        int a = 0;
        int b = 1;
        for(int i=2;i<=n;i++){
            int tmp = a + b;
            a = b;
            b = tmp;
        }
        return b;
    }
}
