package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 18:23
 */
@SuppressWarnings("all")
public class Solution128 {

    private int[] rank;

    public static void main(String[] args) {
        int i = new Solution128().longestConsecutive(new int[]{0,-1});
        System.out.println(i);
    }


    public int longestConsecutive(int[] nums) {
        int[] p = new int[nums.length];
        rank = new int[nums.length];
        for(int i=0;i<nums.length;i++){
            p[i] = i;
            rank[i] = 1;
        }

        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<nums.length;j++){
                if(Math.abs(nums[i] - nums[j]) == 1){
                    union(nums[i],nums[j],p);
                }
            }
        }

        int max = 0;
        for(int i=0;i<rank.length;i++){
            max = Math.max(rank[i],max);
        }
        return max;
    }

    public int find(int x,int[] p){
        return x == p[x] ? x : find(p[x],p);
    }

    public void union(int x,int y,int[] p){
        // rank[x]表示集合x作为根结点的数量
        int xRoot = find(x,p);
        int yRoot = find(y,p);
        if(xRoot == yRoot){
            return;
        }
        if(rank[xRoot] > rank[yRoot]){
            p[yRoot] = xRoot;
            rank[xRoot] += rank[yRoot];
        }else{
            p[xRoot] = yRoot;
            rank[yRoot] += rank[xRoot];
        }
    }
}
