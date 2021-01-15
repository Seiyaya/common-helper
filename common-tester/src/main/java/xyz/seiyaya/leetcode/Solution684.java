package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/13 9:17
 */
public class Solution684 {

    public static void main(String[] args) {
        int[] redundantConnection = new Solution684().findRedundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}});
        System.out.println(Arrays.toString(redundantConnection));
    }

    private int[] rank;

    public int[] findRedundantConnection(int[][] edges) {
        int[] p = new int[edges.length+1];
        rank = new int[edges.length+1];
        for(int i=0;i<=edges.length;i++){
            p[i] = i;
        }

        for(int i=0;i<edges.length;i++){
            boolean union = union(edges[i][0], edges[i][1], p);
            if(!union){
                return new int[]{edges[i][0],edges[i][1]};
            }
        }

        // 去掉一条边，使之成为树
        return new int[]{};
    }

    public int find(int x,int[] p){
        return x == p[x] ? x : find(p[x],p);
    }

    public boolean union(int x,int y,int[] p){
        int xRoot = find(x,p);
        int yRoot = find(y,p);
        if(xRoot == yRoot){
            return false;
        }
        if(rank[xRoot] > rank[yRoot]){
            p[yRoot] = xRoot;
        }else if(rank[xRoot] < rank[yRoot]){
            p[xRoot] = yRoot;
        }else{
            p[xRoot] = yRoot;
            rank[yRoot] += 1;
        }
        return true;
    }
}
