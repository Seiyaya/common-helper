package xyz.seiyaya.leetcode.interview;

/**
 * 求 1+2+...+n ，要求不能使用乘除法、for、while、if、else、switch、case等关键字及条件判断语句（A?B:C）。
 * @author wangjia
 * @version 1.0
 */
public class Interview64 {
    public static void main(String[] args) {
        int i = new Interview64().sumNums(3);
        System.out.println(i);
    }

    public int sumNums(int n) {
        int sum = n;
        boolean flag = n > 0 && ((sum = sum + sumNums(n-1)) > 0);
        return sum;
    }
}
