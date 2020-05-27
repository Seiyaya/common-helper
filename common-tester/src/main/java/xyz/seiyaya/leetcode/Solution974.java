package xyz.seiyaya.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 A，返回其中元素之和可被 K 整除的（连续、非空）子数组的数目。
 *
 * @author wangjia
 * @date 2020/5/27 8:34
 */
public class Solution974 {

    public static void main(String[] args) {
        int result = new Solution974().subarraysDivByK1(new int[]{4, 5, 0, -2, -3, 1}, 5);
        System.out.println(result);
    }


    /**
     * 超时
     * @param A
     * @param K
     * @return
     */
    public int subarraysDivByK(int[] A, int K) {
        int sum = 0;
        int result = 0;
        int[] tmpArray = new int[A.length+1];
        tmpArray[0] = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i];
            tmpArray[i+1] = sum;
        }

        for (int i = 0; i < A.length; i++) {
            if (A[i] % K == 0) {
                result++;
            }
            for (int j = i; j <= A.length; j++) {
                if (((tmpArray[j] - tmpArray[i]) % K == 0) && j > i + 1) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * (a-b)
     * @param A
     * @param K
     * @return
     */
    public int subarraysDivByK1(int[] A, int K) {
        Map<Integer,Integer> maps = new HashMap<>();
        int result = 0;
        int sum = 0;
        maps.put(0,1);
        for(int element:A){
            sum += element;
            int modules = (sum % K + K) % K;
            int same = maps.getOrDefault(modules, 0);
            result += same;
            maps.put(modules,same+1);
        }
        return result;
    }
}
