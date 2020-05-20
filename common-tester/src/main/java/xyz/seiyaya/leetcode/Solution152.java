package xyz.seiyaya.leetcode;

/**
 * 给你一个整数数组 nums ，请你找出数组中乘积最大的连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
 * @author wangjia
 * @date 2020/5/18 8:44
 */
public class Solution152 {

    public static void main(String[] args) {
        int i = new Solution152().maxProduct(new int[]{0,2});
        System.out.println(i);
    }

    public int maxProduct(int[] nums) {
        int max = Integer.MIN_VALUE;
        int tmpMax = 1;
        int tmpMin = 1;
        int tmp;
        for(int i=0;i<nums.length;i++){
            if(nums[i] < 0){
                tmp = tmpMax;
                tmpMax = tmpMin;
                tmpMin = tmp;
            }
            tmpMax = Math.max(tmpMax * nums[i],nums[i]);
            tmpMin = Math.min(tmpMin * nums[i],nums[i]);

            max = Integer.max(tmpMax,max);
        }
        return max;
    }
}
