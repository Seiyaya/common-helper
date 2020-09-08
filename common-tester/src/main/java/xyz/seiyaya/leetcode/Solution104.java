package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/28 8:39
 */
public class Solution104 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        int i = new Solution104().maxDepth(root);
        System.out.println("result:"+i);
    }

    public int maxDepth(TreeNode root) {
        int left = getRootLength(root);
        int right = getRootLength(root);
        return Math.max(left, right);
    }

    private int getRootLength(TreeNode root) {
        if(root == null){
            return 0;
        }
        return Math.max(getRootLength(root.left)+1,getRootLength(root.right)+1);
    }


    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
