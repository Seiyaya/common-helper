package xyz.seiyaya.leetcode;

import java.util.*;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 9:18
 */
public class Solution1202 {

    private int[] rank;


    public static void main(String[] args) {
        String result = new Solution1202().smallestStringWithSwaps("dcab", Arrays.asList(Arrays.asList(0,3), Arrays.asList(1,2)));
        System.out.println(result);
    }


    public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        int length = s.length();
        int[] p = new int[length];
        rank = new int[length];
        for(int i=0;i<p.length;i++){
            p[i] = i;
        }

        // pairs数组中合并，看成一个集合
        for(List<Integer> pair : pairs){
            union(pair.get(0),pair.get(1),p);
        }

        // 建立集合和优先队列对应哈希表
        Map<Integer, PriorityQueue<Character>> map = new HashMap<>();
        for(int i=0;i < length;i++){
            int iRoot = find(i,p);
            PriorityQueue<Character> queue = map.getOrDefault(iRoot, new PriorityQueue<>());
            queue.offer(s.charAt(i));
            map.put(iRoot,queue);
        }
        // 建立字符串，把对应下标所在集合对应的优先队列中的char一个个加入字符串中
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++){
            sb.append(map.get(find(i, p)).poll());
        }
        return sb.toString();
    }

    public int find(int x,int[] p){
        return x == p[x] ? x : find(p[x],p);
    }

    public void union(int x,int y,int[] p){
        int xRoot = find(x,p);
        int yRoot = find(y,p);

        if(xRoot != yRoot){
            if(rank[xRoot] > rank[yRoot]){
                p[yRoot] = xRoot;
            }else if(rank[xRoot] < rank[yRoot]){
                p[xRoot] = yRoot;
            }else{
                p[yRoot] = xRoot;
                rank[xRoot] += 1;
            }
        }
    }
}
