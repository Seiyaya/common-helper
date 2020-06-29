package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version 1.0
 */
public class Solution209 {

    public static void main(String[] args) {
        int i = new Solution209().minSubArrayLen(11, new int[]{1,2,3,4});
        System.out.println(i);
    }

    public int minSubArrayLen(int s, int[] nums) {
        int left = 0;
        int right = 0;
        int sum = 0;
        int result = Integer.MAX_VALUE;
        while(right < nums.length){
            while(sum < s && right<nums.length){
                sum += nums[right++];
            }

            while(sum >= s && left>=0){
                result = Integer.min(result,right - left);
                sum -= nums[left++];
            }
        }
        return result == Integer.MAX_VALUE ? 0 : result;
    }
}
