package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/11 9:26
 */
public class Solution216 {

    public static void main(String[] args) {
        List<List<Integer>> lists = new Solution216().combinationSum3(3, 7);

        System.out.println(lists);
    }

    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(k,n,result,new ArrayList<>(),1);
        return result;
    }

    private void dfs(int k, int n, List<List<Integer>> result, ArrayList<Integer> list,int dept) {
        if(n == 0 && list.size() == k){
            result.add(new ArrayList<>(list));
            return ;
        }
        for(int i=dept;i<=9;i++){
            if(n-i < 0 || list.size() > k){
                break;
            }
            list.add(i);
            dfs(k,n-i,result,list,i+1);
            list.remove(list.size() - 1);
        }
    }
}
