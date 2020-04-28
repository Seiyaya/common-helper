package xyz.seiyaya.leetcode;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangjia
 * @date 2020/4/28 16:26
 */
@Slf4j
public class Solution56 {

    public static void main(String[] args) {
        Solution56 s = new Solution56();
        int[] ints = s.singleNumbers(new int[]{1,2,10,4,1,4,3,3});
        log.info("{}",ints);
    }

    public int[] singleNumbers(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int[] result = new int[2];
        for (int num : nums) {
            if (!set.add(num)) {
                set.remove(num);
            }
        }
        Integer[] integers = set.toArray(new Integer[]{});
        result[0] = integers[0];
        result[1] = integers[1];
        return result;
    }

}
