package xyz.seiyaya.leetcode.interview;

import java.util.Arrays;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/8 9:07
 */
public class Interview11 {

    public static void main(String[] args) {
        int[] ints = new Interview11().divingBoard(1, 1, 10);
        System.out.println(Arrays.toString(ints));
    }


    public int[] divingBoard(int shorter, int longer, int k) {
        if (k == 0) {
            return new int[0];
        }
        int start = k * shorter;
        if (shorter == longer) {
            return new int[]{start};
        }
        int d = longer - shorter;
        int[] result = new int[k + 1];
        for (int i = 0; i < k + 1; i++) {
            result[i] = start + d * i;
        }
        return result;
    }
}
