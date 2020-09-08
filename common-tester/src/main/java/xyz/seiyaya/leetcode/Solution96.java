package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/15 8:33
 */
public class Solution96 {

    public static void main(String[] args) {
        int i = new Solution96().numTrees(3);
        System.out.println("result:"+i);
    }

    public int numTrees(int n) {
        if(n < 1) {
            return 0;
        }
        if(n == 1) {
            return 1;
        }
        int[] t = new int[n+1];
        t[0] = 1;
        t[1] = 1;
        for(int i = 2 ; i <= n ; i++){
            for(int j = 1 ; j <= i ; j++){
                t[i] += t[j-1] * t[i - j];
            }
        }
        return t[n];
    }
}
