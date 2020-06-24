package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * @author wangjia
 * @version 1.0
 */
public class Solution16 {

    public static void main(String[] args) {
        int i = new Solution16().threeSumClosest(new int[]{-1, 2, 1, -4}, 1);

        System.out.println(i);
    }


    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int sum = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            int left = i+1;
            int right = nums.length-1;
            while(left < right){
                int result = nums[left] + nums[right] + nums[i];
                if(Math.abs(result - target) < Math.abs(sum-target)){
                    sum = result;
                }
                if(result > target){
                    right--;
                }else if(result < target){
                    left++;
                }else{
                    return target;
                }
            }
        }
        return sum;
    }
}
