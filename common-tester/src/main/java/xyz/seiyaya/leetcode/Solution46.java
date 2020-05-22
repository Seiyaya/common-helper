package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个 没有重复 数字的序列，返回其所有可能的全排列。
 * @author wangjia
 * @date 2020/5/21 18:27
 */
public class Solution46 {

    public static void main(String[] args) {
        List<List<Integer>> permute = new Solution46().permute(new int[]{});
        System.out.println(permute);
    }

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        dfs(nums,list,0);
        return list;
    }

    private void dfs(int[] nums, List<List<Integer>> list, int count) {
        if(nums.length == count){
            List<Integer> resultList = new ArrayList<>();
            for(int i=0;i<nums.length;i++){
                resultList.add(nums[i]);
            }
            list.add(resultList);
        }

        for(int i=count;i<nums.length;i++){
            swap(nums,i,count);
            dfs(nums,list,count+1);
            swap(nums,i,count);
        }
    }

    private void swap(int[] nums, int i, int count) {
        if(i == count){
            return ;
        }
        int tmp = nums[i];
        nums[i] = nums[count];
        nums[count] = tmp;
    }
}
