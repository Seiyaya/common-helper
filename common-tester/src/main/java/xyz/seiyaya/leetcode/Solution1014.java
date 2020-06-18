package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/6/17 13:43
 */
public class Solution1014 {

    public static void main(String[] args) {
        int i = new Solution1014().maxScoreSightseeingPair(new int[]{4,7,5,8});
        System.out.println(i);
    }

    /**
     * A[i]+A[j]-(j-i)
     * A[i]+i + A[j]-j
     * 先固定前i位的最大值，然后遍历寻找j的位置
     * @param A
     * @return
     */
    public int maxScoreSightseeingPair(int[] A) {
        int result = 0;
        int[] dpI = new int[A.length];
        dpI[0] = A[0];
        for(int i=1;i<A.length;i++){
            dpI[i] = Math.max(dpI[i-1],A[i]+i);
        }
        for(int i=1;i<A.length;i++){
            int tmp = dpI[i-1]+A[i]-i;
            result = Math.max(tmp ,result);
        }
        return result;
    }
}
