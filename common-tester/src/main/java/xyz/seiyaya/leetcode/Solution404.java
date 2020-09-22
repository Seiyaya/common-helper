package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

public class Solution404 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        Solution404 solution404 = new Solution404();
        int i = solution404.sumOfLeftLeaves(root);
        System.out.println("result:"+i);
    }

    public int sumOfLeftLeaves(TreeNode root) {
        if(root == null){
            return 0;
        }
        int result = 0;
        if(root.left != null && root.left.left == null && root.left.right == null){
            result += root.left.val;
        }
        return sumOfLeftLeaves(root.left) + sumOfLeftLeaves(root.right) + result;
    }
}
