package xyz.seiyaya.leetcode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/7 9:16
 */
public class Solution347 {

    public static void main(String[] args) {
        int[] ints = new Solution347().topKFrequent(new int[]{1,2}, 2);

        System.out.println(Arrays.toString(ints));
    }

    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer,Integer> map = new HashMap<>();
        int[] result = new int[k];

        for(int i=0;i<nums.length;i++){
            map.put(nums[i],map.getOrDefault(nums[i],0)+1);
        }

        Queue<Integer> queue = new PriorityQueue<>();
        List<Integer> values = new ArrayList<>();
        map.forEach((key,value)-> values.add(value));
        for(Integer integer :values){
            if(queue.size() < k){
                queue.offer(integer);
            }else if( integer > queue.peek()){
                queue.poll();
                queue.offer(integer);
            }
        }

        Integer peek = queue.peek();

        AtomicInteger count = new AtomicInteger();
        map.forEach((key,value)->{
            if(value >= peek){
                result[count.getAndIncrement()] = key;
            }
        });
        return result;
    }
}
