package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/6/29 8:39
 */
public class Solution215 {

    public static void main(String[] args) {
        int kthLargest = new Solution215().findKthLargest(new int[]{3,2,3,1,2,4,5,5,6}, 4);
        System.out.println(kthLargest);
    }

    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length-k];
    }
}
