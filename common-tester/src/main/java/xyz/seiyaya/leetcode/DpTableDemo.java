package xyz.seiyaya.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjia
 * @date 2020/5/6 14:22
 */
public class DpTableDemo {

    private static Map<Integer,Integer> map = new HashMap<>();

    public static void main(String[] args) {
        /**
         * coins为不同面值的币种凑出amount所需最少的金额
         */
        int amount = 7;
        int[] dpTable = new int[amount + 1];
        for(int i=0;i<amount+1;i++){
            dpTable[i] = Integer.MAX_VALUE;
        }
        int i = coinChangeByDp(new int[]{1, 2, 5}, amount,dpTable);
        System.out.println(i);
    }

    public static int coinChange(int[] coins, int amount){
        if(map.containsKey(amount)){
            return map.get(amount);
        }
        if (amount == 0){
            return 0;
        }
        if( amount < 0){
            return -1;
        }
        int result = Integer.MAX_VALUE;
        for (int coin : coins){
            int tmpResult = coinChange(coins, amount - coin);
            if( tmpResult == -1){
                continue;
            }
            result = Math.min(result, 1 + tmpResult);
            map.put(amount, result);
        }
        return result == Integer.MAX_VALUE ? -1 : result;
    }

    /**
     * 利用dp数组
     * @param coins
     * @param amount
     * @param dp
     * @return
     */
    public static int coinChangeByDp(int[] coins, int amount, int[] dp){
        dp[0] = 0;
        for (int i=0;i< dp.length;i++){
            for(int coin : coins){
                if( i - coin < 0){
                    continue;
                }
                dp[i] = Math.min(dp[i] , 1+dp[i-coin]);
            }
        }
        return dp[amount] == amount +1 ? -1 : dp[amount];
    }
}
