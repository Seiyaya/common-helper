package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/1 8:35
 */
public class Solution718 {

    public static void main(String[] args) {
        int length = new Solution718().findLength(new int[]{1,2,3,2,1}, new int[]{3,2,1,4,7});
        System.out.println(length);
    }

    public int findLength(int[] A, int[] B) {
        int[][] dp = new int[A.length][B.length];
        dp[0][0] = A[0] == B[0] ? 1 : 0;
        int result = 0;
        for(int i=0;i<A.length;i++){
            for(int j=0;j<B.length;j++){
                if(A[i] == B[j]){
                    dp[i][j] = (i>0 && j>0 ? dp[i-1][j-1] : 0)  + 1;
                    result = Math.max(dp[i][j],result);
                }
            }
        }
        return result;
    }
}
