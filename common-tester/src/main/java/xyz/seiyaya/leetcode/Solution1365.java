package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/26 9:28
 */
public class Solution1365 {

    public static void main(String[] args) {
        int[] ints = new Solution1365().smallerNumbersThanCurrent(new int[]{8, 1, 2, 2, 3});
        System.out.println(Arrays.toString(ints));
    }

    public int[] smallerNumbersThanCurrent(int[] nums) {
        int[] arrays = new int[101];
        for (int num : nums){
            arrays[num] += 1;
        }
        for (int i = 1; i < arrays.length; i++){
            arrays[i] += arrays[i - 1];
        }
        for (int i = 0; i < nums.length; i++){
            nums[i] = nums[i] != 0 ? arrays[nums[i] - 1] : 0;
        }
        return nums;
    }
}
