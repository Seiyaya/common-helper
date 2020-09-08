package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 给出集合 [1,2,3,…,n]，其所有元素共有 n! 种排列。
 *
 * 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下：
 */
public class Solution60 {

    public static void main(String[] args) {
        String permutation = new Solution60().getPermutation(3, 3);
        System.out.println("result:"+permutation);
    }


    public String getPermutation(int n, int k) {
        // 生成阶乘字典和剩余数字数组
        HashMap<Integer, Integer> nMap= new HashMap<>();
        ArrayList<Integer> restDigits = new ArrayList<>();
        int currentValue = 1;
        nMap.put(0, 1);
        for (int i = 1; i <= n; ++i){
            currentValue *= i;
            nMap.put(i, currentValue);
            restDigits.add(i);
        }

        StringBuilder sbResult = new StringBuilder();
        for (int i = 0; i < n; ++i){
            int current = 0;
            while (k > nMap.get(n - i - 1) * (current + 1)){
                ++ current;
            }
            sbResult.append(restDigits.get(current));
            restDigits.remove(current);
            k = Math.max(k - nMap.get(n - i - 1) * current, 0);
        }

        return sbResult.toString();
    }
}
