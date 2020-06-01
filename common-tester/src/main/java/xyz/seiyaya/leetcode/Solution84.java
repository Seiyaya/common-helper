package xyz.seiyaya.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
 * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
 */
public class Solution84 {

    public static void main(String[] args) {
        int result = new Solution84().largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3});
        System.out.println(result);
    }

    /**
     * i到j的最小值
     * @param heights
     * @return
     */
    public int largestRectangleArea(int[] heights) {
        int result = 0;
        for (int i = 0; i < heights.length; i++) {
            int height = heights[i];
            int width = 1;
            for (int j = i - 1; j >= 0 && height <= heights[j]; j--) {
                width++;
            }

            for (int j = i + 1; j < heights.length && height <= heights[j]; j++) {
                width++;
            }
            result = Integer.max(result, width * height);
        }
        return result;
    }

    /**
     * 单调栈实现
     * @param heights
     * @link Sweetiee 🍬
     * @return
     */
    public int largestRectangleArea2(int[] heights) {
        // 这里为了代码简便，在柱体数组的头和尾加了两个高度为 0 的柱体。
        int[] tmp = new int[heights.length + 2];
        System.arraycopy(heights, 0, tmp, 1, heights.length);

        Deque<Integer> stack = new ArrayDeque<>();
        int area = 0;
        for (int i = 0; i < tmp.length; i++) {
            // 对栈中柱体来说，栈中的下一个柱体就是其「左边第一个小于自身的柱体」；
            // 若当前柱体 i 的高度小于栈顶柱体的高度，说明 i 是栈顶柱体的「右边第一个小于栈顶柱体的柱体」。
            // 因此以栈顶柱体为高的矩形的左右宽度边界就确定了，可以计算面积🌶️ ～
            while (!stack.isEmpty() && tmp[i] < tmp[stack.peek()]) {
                int h = tmp[stack.pop()];
                area = Math.max(area, (i - stack.peek() - 1) * h);
            }
            stack.push(i);
        }

        return area;
    }
}
