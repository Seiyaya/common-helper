package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/10 9:08
 */
public class Solution40 {

    public static void main(String[] args) {
        List<List<Integer>> lists = new Solution40().combinationSum2(new int[]{10,1,2,7,6,1,5}, 8);

        System.out.println(lists);
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();

        dfs(candidates,target,result,new ArrayList<>(),0);

        List<List<Integer>> collect = result.stream().distinct().collect(Collectors.toList());

        return collect;
    }

    private void dfs(int[] candidates, int target, List<List<Integer>> result, ArrayList<Integer> list, int dept) {
        if(target <= 0){
            if(target == 0){
                ArrayList<Integer> innerList = new ArrayList<>(list);
                Collections.sort(innerList);
                result.add(innerList);
            }
            return ;
        }

        for(int i=dept;i<candidates.length;i++){
            list.add(candidates[i]);
            dfs(candidates,target-candidates[i],result,list,i+1);
            list.remove(list.size()-1);
        }
    }
}
