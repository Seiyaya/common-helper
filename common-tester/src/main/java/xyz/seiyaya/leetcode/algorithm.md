# 算法

## 动态规划
动态规划的重点是找到平衡转移方程，本质上还是进行枚举出最优解，但是枚举的过程中可以简化已经做过的操作降低复杂度
```java
// 凑硬币问题，coins数组硬币的面值凑出amount金额，使用的最小硬币数量
// 1.直接使用递归，递归复杂度的计算 = 子问题个数*解决子问题需要的时间
public static int coinChange(int[] coins, int amount){
    if(amount == 0) return 0;  // 硬币刚刚好凑出整数解
    if(amount < 0)  return -1; // 硬币凑不出该金额
    int result = Integer.MAX_VALUE;
    for( int coin : coins){
        tmpResult = coinChange(coins, amount-coin);
        if( result < 0) continue;
        result = Math.min(result,tmpResult);
    }
    return result == Integer.MAX_VALUE ? -1 : result;
}
//2. 使用辅助数组,利用memo字典记录amount金额需要的最少硬币
public static int coinChange(int[] coins, int amount){
    if(memo.containsKey(amount)){
        return memo.get(amount);
    }   
    if(amount == 0) return 0;  // 硬币刚刚好凑出整数解
    if(amount < 0)  return -1; // 硬币凑不出该金额
    int result = Integer.MAX_VALUE;
    for( int coin : coins){
        tmpResult = coinChange(coins, amount-coin);
        if( result < 0) continue;
        result = Math.min(result,tmpResult);
        memo.put(amount, result);
    }
    return result == Integer.MAX_VALUE ? -1 : result;
}
//3. 使用dp数组 dp[i]表示，i的金额需要的最少硬币
//动态转移方程  dp[i] = min(dp[i-icon] + 1 , dp[i])
public static int coinChangeByDp(int[] coins, int amount, int[] dp){
    for(int i=0;i<dp.length;i++){
        for(int j=0;j<coins.length;j++){
            if(i < coins[j]){
                continue;
            }
            dp[i] = Math.min(dp[i],dp[i-coin] + 1);
        }   
    }
    return dp[amount]  == amount +1 ? -1 : dp[amount];
}
```