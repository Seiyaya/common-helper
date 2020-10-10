package xyz.seiyaya.leetcode.type;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 双指针可以解决的问题
 * @author wangjia
 * @version 1.0
 * @date 2020/9/28 15:41
 */
public class TwoPointer {

    /**
     * 两束之和
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
     *
     * 双指针解决的要对数组进行排序，实际上复杂度会比hash稍微高一点
     */
    @Test
    public void testSolution1(){
        int[] ints = twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println(Arrays.toString(ints));
    }

    /**
     * hash解决
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> maps = new HashMap<>();
        for(int i=0;i<nums.length;i++){
            Integer integer = maps.get(target - nums[i]);
            if( integer == null){
                maps.put(nums[i],i);
            }else{
                return new int[]{integer,i};
            }
        }
        return new int[]{};
    }

    /**
     * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。
     * 在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
     * 你不能倾斜容器，且 n 的值至少为 2。
     */
    @Test
    public void testSolution11(){
        int area = maxArea(new int[]{1,8,6,2,5,4,8,3,7});
        System.out.println(area);
    }

    public int maxArea(int[] height) {
        int maxValue = 0;
        int left = 0;
        int right = height.length-1;
        while(left < right){
            maxValue = height[left]  < height[right]
                    ? Math.max(maxValue,(right-left) * height[left++]): Math.max(maxValue,(right-left) * height[right--]);
        }
        return maxValue;
    }

    /**
     * 接雨水
     * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
     */
    @Test
    public void testSolution42(){
        int trap = trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1});
        System.out.println("result:"+trap);
    }

    public int trap(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int bucketHeight = 0;
        int result = 0;
        while(left < right){
            int minHeight = Math.min(height[left], height[right]);
            bucketHeight = Math.max(bucketHeight,minHeight);
            result += height[left] >= height[right] ? (bucketHeight - height[right--]) : (bucketHeight - height[left++]);
        }
        return result;
    }

    public int trap2(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int result = 0;
        // 找到左边和右边的合适位置
        while(left < right && height[left] <= height[left+1]){
            left++;
        }
        while(left < right && height[right] <= height[right-1]){
            right--;
        }

        while(left < right){
            int leftValue = height[left];
            int rightValue = height[right];
            if(leftValue <= rightValue){
                while(left < right && leftValue >= height[++left]){
                    result += leftValue - height[left];
                }
            }else{
                while(left < right && rightValue >= height[--right]){
                    result += rightValue - height[right];
                }
            }
        }
        return result;
    }

    /**
     * 给定一个已按照升序排列 的有序数组，找到两个数使得它们相加之和等于目标数。
     * 函数应该返回这两个下标值 index1 和 index2，其中 index1 必须小于 index2。
     */
    @Test
    public void testSolution167(){
        int[] ints = twoSum167(new int[]{2, 7, 11, 15}, 9);
        System.out.println(Arrays.toString(ints));
    }

    public int[] twoSum167(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length-1;
        while(left < right){
            int count = numbers[left] + numbers[right];
            if(count < target){
                left++;
            }else if(count > target){
                right--;
            }else{
                return new int[]{left+1,right+1};
            }
        }
        return new int[]{};
    }
}
