package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/19 11:23
 */
public class Solution1584 {


    public static void main(String[] args) {
        int i = new Solution1584().minCostConnectPoints(new int[][]{
                {0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}
        });

        System.out.println(i);
    }


    public int getMH(int[] a, int[] b){
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }

    public int getMinMH(int[][] a, int[] b, int n){
        int min = Integer.MAX_VALUE;
        for(int i = 0;i < n;i++){
            if(getMH(a[i], b) < min){
                min = getMH(a[i], b);
            }
        }
        return min;
    }

    public int minCostConnectPoints(int[][] points) {
        if(points.length == 0)  {
            return 0;
        }
        boolean[] flag = new boolean[points.length];
        for(int i = 0;i < flag.length;i++){
            flag[i] = false;
        }
        int[][] tree = new int[points.length][2];
        tree[0] = points[0];
        flag[0] = true;
        int index = 0;
        int cost = 0;
        for(int k = 1;k < points.length;k++){
            int min = Integer.MAX_VALUE;
            for(int i = 0;i < points.length;i++){
                if(flag[i] == false && getMinMH(tree, points[i],k) < min){
                    min = getMinMH(tree, points[i],k);
                    index = i;
                }
            }
            cost += min;
            flag[index] = true;
            tree[k] = points[index];
        }
        return cost;
    }
}
