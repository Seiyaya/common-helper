package xyz.seiyaya.leetcode;

/**
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 *
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 */
public class Solution70 {

    public static void main(String[] args) {
        int i = new Solution70().climbStairs(31);
        System.out.println(i);
    }

    /**
     * dp[i] = dp[i-1] + 1
     * dp[i] = dp[i-2] + 2
     * dp[i] = Math.max(dp[i-1] + 1,dp[i-2] + 2)
     * @param n
     * @return
     */
    public int climbStairs(int n) {
        if(n <= 2){
            return n;
        }
        int[] dp = new int[n+1];
        dp[1] = 1;
        dp[2] = 2;
        for(int i=3;i<=n;i++){
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }


    public int climbStairs2(int n) {
        if(n <= 2){
            return n;
        }
        return climbStairs(n-1)+climbStairs(n-2);
    }
}
