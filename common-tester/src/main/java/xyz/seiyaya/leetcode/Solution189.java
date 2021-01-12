package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/8 13:58
 */
public class Solution189 {

    public static void main(String[] args) {
        int[] arr = {-1,-100,3,99};
        new Solution189().rotate(arr,1);
        System.out.println(Arrays.toString(arr));
    }

    public void rotate(int[] nums, int k) {
        if( k == 0 || nums.length <=1){
            return ;
        }
        if(k > nums.length ){
            k = k % nums.length;
        }
        swap(nums,0,nums.length-1);
        swap(nums,0,k-1);
        swap(nums,k,nums.length-1);
    }

    public void swap(int[] nums,int start , int end){
        while(start < end){
            int tmp = nums[start];
            nums[start] = nums[end];
            nums[end] = tmp;
            start++;
            end--;
        }
    }
}
