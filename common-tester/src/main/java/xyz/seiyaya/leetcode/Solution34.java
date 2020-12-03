package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * @author wangjia
 * @version v1.0
 * @date 2020/12/1 9:31
 */
public class Solution34 {

    public static void main(String[] args) {
        int[] ints = new Solution34().searchRange(new int[]{1}, 1);

        System.out.println(Arrays.toString(ints));
    }


    public int[] searchRange(int[] nums, int target) {
        int[] res = new int[] {-1, -1};
        res[0] = binarySearch(nums, target, true);
        res[1] = binarySearch(nums, target, false);
        return res;
    }

    /**
     * leftOrRight为true找左边界 false找右边界
     */
    public int binarySearch(int[] nums, int target, boolean leftOrRight) {
        int res = -1;
        int left = 0, right = nums.length - 1, mid;
        while(left <= right) {
            mid = left + (right - left) / 2;
            if(target < nums[mid]){
                right = mid - 1;
            } else if(target > nums[mid]){
                left = mid + 1;
            } else {
                res = mid;
                //处理target == nums[mid]
                if(leftOrRight){
                    right = mid - 1;
                } else{
                    left = mid + 1;
                }
            }
        }
        return res;
    }
}
