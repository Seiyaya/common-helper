package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * 给定一个按非递减顺序排序的整数数组 A，返回每个数字的平方组成的新数组，要求也按非递减顺序排序。
 * @author wangjia
 * @version 1.0
 * @date 2020/10/16 9:07
 */
public class Solution977 {

    public static void main(String[] args) {
        int[] ints = new Solution977().sortedSquares(new int[]{-3,0,3});
        System.out.println(Arrays.toString(ints));
    }

    public int[] sortedSquares(int[] A) {
        int left = 0;
        int right = A.length-1;
        int[] result = new int[A.length];
        int count = A.length -1;
        while(count >= 0){
            result[count--] = Math.abs(A[left]) > Math.abs(A[right]) ? A[left]*A[left++] : A[right]*A[right--];
        }
        return result;
    }
}
