package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/11/3 16:02
 */
public class Solution941 {

    public static void main(String[] args) {
        boolean b = new Solution941().validMountainArray(new int[]{0, 3, 2, 1});
        System.out.println("山脉数组:"+b);
    }

    public boolean validMountainArray(int[] A) {
        int end = A.length;
        int start = 0;
        while(start < end - 1 && A[start+1] > A[start]){
            start++;
        }
        // 单调递增和单调递减的情况没有山脉
        if(start == end-1 || start == 0) {
            return false;
        }
        while(start < end-1 && A[start+1] < A[start]){
            start++;
        }
        return start == end - 1;
    }
}
