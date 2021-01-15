package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/15 9:37
 */
public class Solution947 {

    public static void main(String[] args) {
        int i = new Solution947().removeStones(new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 2}, {2, 1}, {2, 2}});
        System.out.println("result:"+i);
    }

    private int[] p;

    private int[] rank;

    public int removeStones(int[][] stones) {
        p = new int[stones.length];
        rank = new int[stones.length];
        for(int i=0;i<p.length;i++){
            p[i] = i;
        }

        // 能够联合表示能够移出石头
        int result = 0;
        for(int i=0;i<stones.length;i++){
            for(int j=i+1;j<stones.length;j++){
                if(stones[i][0] == stones[j][0] || stones[i][1] == stones[j][1]){
                    union(i,j);
                }
            }
        }

        for(int i=0;i<p.length;i++){
            if(p[i] == i){
                result++;
            }
        }
        return p.length - result;
    }

    public int find(int x){
        return x == p[x] ? x : find(p[x]);
    }

    public boolean union(int x,int y){
        int xRoot = find(x);
        int yRoot = find(y);
        if(xRoot == yRoot){
            return false;
        }

        if(rank[xRoot] > rank[yRoot]){
            p[yRoot] = xRoot;
        }else if(rank[xRoot] < rank[yRoot]){
            p[xRoot] = yRoot;
        }else{
            p[yRoot] = xRoot;
            rank[xRoot] += 1;
        }
        return true;
    }
}
