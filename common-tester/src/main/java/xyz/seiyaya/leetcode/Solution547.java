package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 18:00
 */
@SuppressWarnings("all")
public class Solution547 {

    private int[] rank;

    public static void main(String[] args) {
        int circleNum = new Solution547().findCircleNum(new int[][]{{
                1, 0,0
        }, {0, 1, 0}, {0, 0, 1}});
        System.out.println(circleNum);
    }



    public int findCircleNum(int[][] M) {
        int length = M.length;
        int[] p = new int[length];
        rank = new int[length];

        for(int i=0;i<p.length;i++){
            p[i] = i;
        }

        for(int i=0;i<M.length;i++){
            for(int j=i+1;j<M.length;j++){
                if(M[i][j] != 0){
                    union(i,j,p);
                    length --;
                }
            }
        }
        return length;
    }

    public int find(int x, int[] p){
        return x == p[x] ? x : find(p[x],p);
    }

    public void union(int x,int y,int[] p){
        int xRoot = find(x,p);
        int yRoot = find(y,p);
        if(xRoot == yRoot){
            return ;
        }
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
