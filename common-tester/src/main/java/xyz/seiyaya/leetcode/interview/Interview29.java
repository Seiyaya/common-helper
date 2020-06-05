package xyz.seiyaya.leetcode.interview;

import java.util.Arrays;

/**
 * 输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字。
 * @author wangjia
 * @version 1.0
 * @date 2020/6/5 9:01
 */
public class Interview29 {

    public static void main(String[] args) {
        int[] ints = new Interview29().spiralOrder(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        System.out.println(Arrays.toString(ints));
    }

    public int[] spiralOrder(int[][] matrix) {
        if(matrix.length == 0){
            return new int[]{};
        }
        int count = matrix.length * matrix[0].length;
        int[] result = new int[count];
        boolean[][] visited = new boolean[matrix.length][matrix[0].length];
        int row = 0;
        int col = 0;
        // 0 表示横着走 1表示竖着走  2表示反向横着走 3表示反向竖着走
        int direct = 0;
        result[0] = matrix[0][0];
        visited[0][0] = true;
        for(int i=1;i<count;){
            boolean failFlag = false;
            switch (direct){
                case 0:
                    col++;
                    if(col == matrix[0].length || visited[row][col]){
                        col--;
                        direct = 1;
                        failFlag = true;
                    }
                    break;
                case 1:
                    row++;
                    if(row == matrix.length || visited[row][col]){
                        row--;
                        direct = 2;
                        failFlag = true;
                    }
                    break;
                case 2:
                    col--;
                    if(col == -1 || visited[row][col]){
                        col++;
                        direct = 3;
                        failFlag = true;
                    }
                    break;
                case 3:
                    row--;
                    if(row == -1 || visited[row][col]){
                        row++;
                        direct = 0;
                        failFlag = true;
                    }
                    break;
                default:
            }
            if(!failFlag){
                visited[row][col] = true;
                result[i] = matrix[row][col];
                i++;
            }
        }

        return result;
    }
}
