package xyz.seiyaya.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组和一个整数 k，你需要找到该数组中和为 k 的连续的子数组的个数。
 * 输入:nums = [1,1,1], k = 2
 * 输出: 2 , [1,1] 与 [1,1] 为两种不同的情况。
 * 数组的长度为 [1, 20,000]。
 * 数组中元素的范围是 [-1000, 1000] ，且整数 k 的范围是 [-1e7, 1e7]。
 * @author wangjia
 * @date 2020/5/15 8:39
 */
public class Solution560 {

    public static void main(String[] args) {
        int i = new Solution560().subarraySum1(new int[]{1,2,3,4,5}, 5);
        System.out.println(i);
    }

    public int subarraySum(int[] nums, int k) {
        int count = 0;
        for(int i=0;i<nums.length;i++){
            int j = i;
            int tmp = k;
            while(j < nums.length){
                tmp = tmp - nums[j];
                if(tmp == 0){
                    count++;
                }
                j++;
            }
        }
        return count;
    }

    /**
     * 此处求k ==> (sum - k)的次数
     * 而sum-k是从0开始的数组，一次循环即可
     * @param nums
     * @param k
     * @return
     */
    public int subarraySum1(int[] nums, int k) {
        Map<Integer,Integer> maps = new HashMap<>();
        maps.put(0,1);
        int sum = 0, result = 0;
        for(int i=0;i<nums.length;i++){
            sum += nums[i];
            result += maps.getOrDefault(sum-k,0);
            maps.put(sum,maps.getOrDefault(sum,0)+1);
        }
        return result;
    }
}
