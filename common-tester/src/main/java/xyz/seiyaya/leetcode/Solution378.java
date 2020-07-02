package xyz.seiyaya.leetcode;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/2 8:39
 */
public class Solution378 {

    public static void main(String[] args) {
        int i = new Solution378().kthSmallest(new int[][]{
                {1, 5, 9},
                {10, 11, 13},
                {12, 13, 15}
        }, 8);
        System.out.println(i);
    }

    public int kthSmallest(int[][] matrix, int k) {
        int len = matrix.length * matrix[0].length;
        int[] num = new int[len];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                num[i * matrix[0].length + j] = matrix[i][j];
            }
        }
        Arrays.sort(num);
        return num[k-1];
    }


    public int kthSmallest1(int[][] matrix, int k) {
        //最大堆
        PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> b - a);
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                //不足k个，直接加入
                if (queue.size() < k) {
                    queue.add(anInt);
                    //待加入的更小，则去掉对顶元素
                } else if (anInt < queue.peek()) {
                    queue.poll();
                    queue.add(anInt);
                }
            }
        }
        return queue.peek();
    }
}
