package xyz.seiyaya.leetcode;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 实现lru缓存
 * @author wangjia
 * @date 2020/5/25 8:50
 */
public class Solution146 {

    public static void main(String[] args) {
        LRUCache cache = new LRUCache( 2 );

        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));
        // 该操作会使得密钥 2 作废
        cache.put(3, 3);
        System.out.println(cache.get(2));
        // 该操作会使得密钥 1 作废
        cache.put(4, 4);
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
    }

    private static class LRUCache {

        private int capacity;

        // 或者这里直接覆盖LinkedHashMap的removeEldestEntry方法，判断条件为 this.size() > capacity
        private LinkedHashMap<Integer,Integer> maps = null;


        public LRUCache(int capacity) {
            this.capacity = capacity;
            maps = new LinkedHashMap<>(capacity);
        }

        public int get(int key) {
            Integer result = maps.get(key);
            if(result != null){
                maps.remove(key);
                maps.put(key,result);
                return result;
            }
            return -1;
        }

        public void put(int key, int value) {
            if(maps.containsKey(key)){
                maps.remove(key);
            }else if(capacity == maps.size()){
                Iterator<Map.Entry<Integer, Integer>> iterator = maps.entrySet().iterator();
                iterator.next();
                iterator.remove();
            }
            maps.put(key,value);
        }
    }
}
