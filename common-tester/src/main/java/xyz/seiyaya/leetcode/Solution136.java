package xyz.seiyaya.leetcode;

/**
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 * @author wangjia
 * @date 2020/5/14 8:45
 */
public class Solution136 {
    public static void main(String[] args) {
        int i = new Solution136().singleNumber(new int[]{1,1,2,2,0});
        System.out.println(i);

        System.out.println(10<<1);
    }

    public int singleNumber(int[] nums) {
        int a = 0;
        for(int num : nums){
            a = a ^ num;
        }
        return a;
    }
}
