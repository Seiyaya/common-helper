package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * 给你一个长度为 n 的整数数组 nums，其中 n > 1，返回输出数组 output ，其中 output[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积。
 * 请不要使用除法，且在 O(n) 时间复杂度内完成此题。
 * @author wangjia
 * @version 1.0
 * @date 2020/6/4 8:50
 */
public class Solution238 {

    public static void main(String[] args) {
        int[] ints = new Solution238().productExceptSelf(new int[]{3, 4, 5, 6});
        System.out.println(Arrays.toString(ints));
    }

    public int[] productExceptSelf(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = 1;
        for(int i=1;i<nums.length;i++){
            dp[i] = dp[i-1] * nums[i-1];
        }
        int tmp = 1;
        for(int i=nums.length-1;i>=0;i--){
            dp[i] *= tmp;
            tmp *= nums[i];
        }
        return dp;
    }
}
