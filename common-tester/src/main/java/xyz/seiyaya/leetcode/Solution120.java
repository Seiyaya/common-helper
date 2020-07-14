package xyz.seiyaya.leetcode;

import java.util.Arrays;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/14 8:51
 */
public class Solution120 {

    public static void main(String[] args) {
        List<List<Integer>> list = Arrays.asList(Arrays.asList(2),Arrays.asList(3,4),Arrays.asList(6,5,7),Arrays.asList(4,1,8,3));
        int i = new Solution120().minimumTotal(list);
        System.out.println("result="+i);
    }

    public int minimumTotal(List<List<Integer>> triangle) {
        if (triangle == null || triangle.size() == 0){
            return 0;
        }
        // 加1可以不用初始化最后一层
        int[][] dp = new int[triangle.size()+1][triangle.size()+1];

        for (int i = triangle.size()-1; i>=0; i--){
            List<Integer> curTr = triangle.get(i);
            for(int j = 0 ; j< curTr.size(); j++){
                dp[i][j] = Math.min(dp[i+1][j], dp[i+1][j+1]) + curTr.get(j);
            }
        }
        return dp[0][0];
    }
}
