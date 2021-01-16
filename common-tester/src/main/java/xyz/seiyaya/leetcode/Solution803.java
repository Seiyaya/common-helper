package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * 打砖块
 */
public class Solution803 {

    public static void main(String[] args) {
        int[] ints = new Solution803().hitBricks(new int[][]{{1, 0, 0, 0}, {1, 1, 1, 0}}, new int[][]{{1, 0}});
        System.out.println(Arrays.toString(ints));
    }

    public int[] hitBricks(int[][] grid, int[][] hits) {
        int hight = grid.length;
        int width = grid[0].length;
        // 额外记录用的“墙壁”
        int totleSize = hight*width;
        int[] newTree = new int[totleSize + 1];
        int[] size = new int[totleSize + 1];
        // 初始化并查集，每个节点连线之前默认为1
        for(int i = 0; i < totleSize + 1; i++){
            newTree[i] = i;
            size[i] = 1;
        }
        int[][] newNums = new int[hight][width];
        for(int i = 0; i < hight; i++){
            for(int j = 0; j < width; j++){
                newNums[i][j] = grid[i][j];
            }
        }
        // 初始化一个砸掉目标石头的二维图
        for(int i = 0; i < hits.length; i++){
            newNums[hits[i][0]][hits[i][1]] = 0;
        }
        for(int i = 0; i < hight; i++){
            for(int j = 0; j < width; j++){
                if(newNums[i][j] == 1){
                    // 第一排和“墙壁”贴合，直接关联石头和墙壁
                    if(i == 0){
                        union(newTree, size, i*width + j, totleSize);
                    }
                    // 上边有石头就连起来
                    if(i > 0 && newNums[i-1][j] == 1){
                        union(newTree, size, i*width + j, (i-1)*width + j);
                    }
                    // 左边有石头就连起来
                    if(j > 0 && newNums[i][j-1] == 1){
                        union(newTree, size, i*width + j, i*width + j - 1);
                    }
                }
            }
        }
        int[] res = new int[hits.length];
        // 定义上下左右四个方向
        int[][] direction = {{0,1},{1,0},{0,-1},{-1,0}};
        for(int i = hits.length-1; i >= 0; i--){
            int row = hits[i][0];
            int col = hits[i][1];
            // 如果砸之前就没有石头，返回0
            if(grid[row][col] == 0){
                continue;
            }
            // 补上石头之前与墙壁相连的石头的长度
            int initSize = size[find(newTree, totleSize)];
            if(row == 0){
                union(newTree, size, col, totleSize);
            }
            for(int[] dir : direction){
                int dirH = row + dir[0];
                int dirW = col + dir[1];
                if(dirH >= 0 && dirH < hight && dirW >= 0 && dirW < width){
                    // 四个方向有石头的都连起来
                    if(newNums[dirH][dirW] == 1){
                        union(newTree, size, row*width+col, dirH*width+dirW);
                    }
                }
            }
            // size[find(newTree, totleSize)]是补上石头之后与墙壁相连的石头的长度
            res[i] = Math.max(0, size[find(newTree, totleSize)] - initSize -1);
            newNums[row][col] = 1;
        }
        return res;
    }

    public int find(int[] p, int x){
        int root = x;
        if(p[x] != x){
            root = find(p, p[x]);
        }
        return root;
    }

    public void union(int[] p, int[] q, int x, int y){
        int xVal = find(p, x);
        int yVal = find(p, y);
        if(xVal == yVal){
            return;
        }
        p[xVal] = yVal;
        q[yVal] += q[xVal];
    }
}
