package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/9 9:13
 */
public class Solution39 {

    public static void main(String[] args) {
        List<List<Integer>> lists = new Solution39().combinationSum(new int[]{2,3,6,7}, 8);

        System.out.println(lists);
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> innerResult = new ArrayList<>();
        dfs(candidates,target,result,innerResult,0);

        // 去重
        List<List<Integer>> collect = result.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    private void dfs(int[] candidates, int target, List<List<Integer>> result, List<Integer> innerResult,int begin) {
        if(target <= 0){
            if(target == 0){
                ArrayList<Integer> list = new ArrayList<>(innerResult);
                Collections.sort(list);
                result.add(list);
            }
            return ;
        }

        for(int i=begin;i<candidates.length;i++){
            innerResult.add(candidates[i]);
            dfs(candidates,target-candidates[i],result,innerResult,i);
            innerResult.remove(innerResult.size()-1);
        }
    }
}
