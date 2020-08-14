package xyz.seiyaya.leetcode.type;

import org.junit.Test;

/**
 * 位运算
 * @author wangjia
 * @version 1.0
 * @date 2020/8/12 10:55
 */
public class BitCalc {

    /**
     * 异或实现两数交换
     */
    @Test
    public void testXORSwap(){
        int a = 3;
        int b = 6;

        a = a ^ b;
        b = a ^ b;
        a = a ^ b;

        /**
         * 公式推导
         * a = a ^ b;
         * b = a ^ b = (a ^ b) ^ b = a ^ ( b ^ b) = a ^ 0 = a
         * a = a ^ b = (a ^ b) ^ a = (a ^ a) ^ b = 0 ^ b = b
         */

        System.out.println(String.format("a = %s b = %s ", a , b));
    }

    /**
     * 异或实现查找数组中不同的元素，其他元素都是成对出现的
     * 原理   a ^ a = 0   0 ^ a = a
     */
    @Test
    public void testXORFindDifferent(){
        int[] array = {1,1,2,2,3,3,4};

        int start = array[0];
        for(int i=1;i<array.length;i++){
            start ^= array[i];
        }

        System.out.println(start);
    }
}
