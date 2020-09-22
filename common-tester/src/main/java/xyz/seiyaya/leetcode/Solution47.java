package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给定一个可包含重复数字的序列，返回所有不重复的全排列
 * @author wangjia
 * @date 2020/5/22 17:53
 */
public class Solution47 {

    public static void main(String[] args) {
        List<List<Integer>> lists = new Solution47().permuteUnique(new int[]{1, 1, 2, 3});
        System.out.println(lists);
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(nums,0,result);
        return result;
    }

    private void dfs(int[] nums, int count, List<List<Integer>> list) {
        if(nums.length == count){
            List<Integer> inner = new  ArrayList<>();
            Arrays.stream(nums).forEach(inner::add);
            list.add(inner);
            return ;
        }

        for(int i=count;i<nums.length;i++){
            if(canSwap(nums,count,i)){
                swap(nums,i,count);
                dfs(nums,count+1,list);
                swap(nums,i,count);
            }
        }
    }

    /**
     * 判断是否交换过
     * @param nums
     * @param begin
     * @param end
     * @return
     */
    private boolean canSwap(int[] nums, int begin, int end) {
        for(int i=begin;i<end;i++){
            if(nums[i] == nums[end]){
                return false;
            }
        }
        return true;
    }

    private void swap(int[] nums,int start,int end){
        int tmp = nums[start];
        nums[start] = nums[end];
        nums[end] = tmp;
    }
}
