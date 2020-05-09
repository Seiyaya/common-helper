package xyz.seiyaya.leetcode;

/**
 * 最大正方形
 * @author wangjia
 * @date 2020/5/8 14:17
 */
public class Solution221 {

    public static void main(String[] args) {
        /**
         * dp[i][j] = 1 + min( dp[i-1][j] , dp[i-1][j-1] , dp[i][j-1])
         */
        int i = new Solution221().maximalSquare(new char[][]{
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}
        });
        System.out.println("area:"+i);
    }

    private int maximalSquare(char[][] matrix) {
        if(matrix.length < 1){
            return 0;
        }
        int max = 0;
        int[][] dp = new int[matrix.length+1][matrix[0].length+1];
        for(int i=1;i<=matrix.length;i++){
            for(int j=1;j<=matrix[0].length;j++){
                if(matrix[i-1][j-1] == '1'){
                    dp[i][j] = 1 + Math.min(Math.min(dp[i][j-1],dp[i-1][j]),dp[i-1][j-1]);
                    max = Math.max(max, dp[i][j]);
                }
            }
        }
        return max * max;
    }
}
