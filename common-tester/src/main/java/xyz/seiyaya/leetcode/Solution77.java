package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/8 9:18
 */
public class Solution77 {

    public static void main(String[] args) {
        List<List<Integer>> combine = new Solution77().combine(5, 2);
        System.out.println(combine);
    }

    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        if( k > n || n==0){
            return result;
        }

        dfs(1,n,k,result,new ArrayList<>(),0);

        return result;
    }

    private void dfs(int dept, int n, int k, List<List<Integer>> result,List<Integer> list,int count) {
        if(count == k){
            result.add(new ArrayList<>(list));
            return ;
        }

        for(int i=dept;i<=n;i++){
            list.add(i);
            dfs(i+1,n,k,result,list,count+1);
            list.remove(list.size()-1);
        }
    }
}
