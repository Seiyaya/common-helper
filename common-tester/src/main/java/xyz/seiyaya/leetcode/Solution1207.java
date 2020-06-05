package xyz.seiyaya.leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/6/5 16:04
 */
public class Solution1207 {

    public static void main(String[] args) {
        boolean b = new Solution1207().uniqueOccurrences(new int[]{-4,3,3});
        System.out.println(b);
    }

    public boolean uniqueOccurrences(int[] arr) {
        Map<Integer,Integer> maps = new HashMap<>();
        for(int i=0;i<arr.length;i++){
            maps.put(arr[i],maps.getOrDefault(arr[i],0)+1);
        }

        System.out.println(maps);

        Set<Integer> set = new HashSet<>();
        for (Integer value : maps.values()) {
            if(!set.add(value)){
                return false;
            }
        }
        return true;
    }
}
