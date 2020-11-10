package xyz.seiyaya.leetcode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 我们有一个由平面上的点组成的列表 points。需要从中找出 K 个距离原点 (0, 0) 最近的点。
 * topk问题 直接用优先队列解决即可
 * @author wangjia
 * @version 1.0
 * @date 2020/11/9 8:39
 */
public class Solution973 {

    public static void main(String[] args) {
        int[][] ints = new Solution973().kClosest(new int[][]{{3,3}, {5,-1},{-2,4}}, 2);
        for(int[] tmp:ints){
            System.out.println(Arrays.toString(tmp));
        }
    }

    public int[][] kClosest(int[][] points, int K) {
        PriorityQueue<ArrayValue> priorityQueue = new PriorityQueue<>(Comparator.reverseOrder());
        for(int[] tmp : points){
            ArrayValue arrayValue = new ArrayValue(tmp);
            if(priorityQueue.size() < K){
                priorityQueue.add(arrayValue);
            }else if(arrayValue.compareTo(priorityQueue.peek()) < 0){
                priorityQueue.poll();
                priorityQueue.add(arrayValue);
            }
        }
        int[][] result = new int[K][2];
        for(int i=0;i<K;i++){
            ArrayValue poll = priorityQueue.poll();
            result[i] = poll.num;
        }
        return result;
    }

    private class ArrayValue implements Comparable{
        int[] num;
        int value;

        public ArrayValue(int[] num){
            this.num = num;
            this.value = num[0] * num[0] + num[1] * num[1];
        }

        @Override
        public int compareTo(Object o) {
            ArrayValue arrayValue = (ArrayValue) o;
            return this.value - arrayValue.value;
        }
    }
}
