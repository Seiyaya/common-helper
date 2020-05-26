package xyz.seiyaya.leetcode;

/**
 * 给定一个包含 n + 1 个整数的数组 nums，其数字都在 1 到 n 之间（包括 1 和 n），可知至少存在一个重复的整数。假设只有一个重复的整数，找出这个重复的数。
 * 不能更改原数组（假设数组是只读的）。
 * 只能使用额外的 O(1) 的空间。
 * 时间复杂度小于 O(n2) 。
 * 数组中只有一个重复的数字，但它可能不止重复出现一次。
 * @author wangjia
 * @date 2020/5/26 8:53
 */
public class Solution287 {

    public static void main(String[] args) {
        int duplicate = new Solution287().findDuplicate(new int[]{3, 1, 3, 4, 2});
        System.out.println(duplicate);
    }

    public int findDuplicate(int[] nums) {
        int fast = 0;
        int slow = 0;
        for(;slow != fast || fast == 0;){
            slow = nums[slow];
            fast = nums[nums[fast]];
        }
        for(int i=0; slow != i;i=nums[i]){
            slow = nums[slow];
        }
        return slow;
    }
}
