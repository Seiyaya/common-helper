package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * 在一个火车旅行很受欢迎的国度，你提前一年计划了一些火车旅行。在接下来的一年里，你要旅行的日子将以一个名为 days 的数组给出。每一项是一个从 1 到 365 的整数。
 * @author wangjia
 * @date 2020/5/6 16:29
 */
public class Solution983 {

    public static void main(String[] args) {
        int i = mincostTickets(new int[]{1, 4, 6, 7, 8, 20}, new int[]{2, 7, 15});
        System.out.println(i);
    }

    public static int mincostTickets(int[] days, int[] costs) {
        int[] dp = new int[366];
        Arrays.fill(dp, 0);
        int n = days[days.length - 1];
        for(int i = 0;i<days.length;i++){
            dp[days[i]] = -1;
        }
        for(int i=1;i<=n;i++){
            if(dp[i] == 0){
                dp[i] = dp[i-1];
            }else{
                int a = dp[i-1] + costs[0];
                int b = i >= 7 ? dp[i-7] + costs[1] : costs[1];
                int c = i >= 30 ? dp[i-30]+costs[2] : costs[2];
                dp[i] = Math.min(Math.min(a,b),c);
            }
        }
        return dp[n];
    }
}
