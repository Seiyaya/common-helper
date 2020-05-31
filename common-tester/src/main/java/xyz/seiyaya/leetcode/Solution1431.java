package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个数组 candies 和一个整数 extraCandies ，其中 candies[i] 代表第 i 个孩子拥有的糖果数目。
 * 对每一个孩子，检查是否存在一种方案，将额外的 extraCandies 个糖果分配给孩子们之后，此孩子有 最多 的糖果。注意，允许有多个孩子同时拥有 最多 的糖果数目。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/kids-with-the-greatest-number-of-candies
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution1431 {

    public static void main(String[] args) {
        List<Boolean> booleans = new Solution1431().kidsWithCandies(new int[]{4, 2, 1, 1, 2}, 1);
        System.out.println(booleans);
    }

    public List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
        List<Boolean> list = new ArrayList<>(candies.length);
        int max = -1;
        for (int i = 0; i < candies.length; i++) {
            max = Integer.max(max, candies[i]);
        }
        for (int i = 0; i < candies.length; i++) {
            if (candies[i] + extraCandies >= max) {
                list.add(Boolean.TRUE);
            } else {
                list.add(Boolean.FALSE);
            }
        }
        return list;
    }
}
