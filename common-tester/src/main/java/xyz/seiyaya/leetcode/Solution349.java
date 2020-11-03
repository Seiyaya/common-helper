package xyz.seiyaya.leetcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/11/2 9:43
 */
public class Solution349 {

    public static void main(String[] args) {
        int[] intersection = new Solution349().intersection(new int[]{1, 2, 2, 1}, new int[]{2, 2});
        System.out.println(Arrays.toString(intersection));
    }


    public int[] intersection(int[] nums1, int[] nums2) {
        List<Integer> numList1 = Arrays.stream(nums1).boxed().distinct().collect(Collectors.toList());
        List<Integer> numList2 = Arrays.stream(nums2).boxed().collect(Collectors.toList());
        numList1.retainAll(numList2);
        int[] result = new int[numList1.size()];
        for(int i=0;i<numList1.size();i++){
            result[i] = numList1.get(i);
        }
        return result;
    }
}
