package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * 给定一个二叉树，检查它是否是镜像对称的。
 * 即每个根结点的左子树和右子树是一样的
 */
public class Solution101 {

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        treeNode.left = new TreeNode(2);
        treeNode.right = new TreeNode(2);
        treeNode.left.left = new TreeNode(3);
        treeNode.left.right = new TreeNode(4);

        treeNode.right.left = new TreeNode(4);
//        treeNode.right.right = new TreeNode(3);

        System.out.println(new Solution101().isSymmetric(treeNode));
    }

    public boolean isSymmetric(TreeNode root) {
        return judgeSymmetric(root,root);
    }

    private boolean judgeSymmetric(TreeNode left, TreeNode right) {
        if(left == null && right == null){
            return true;
        }
        if(left != null && right != null && left.val == right.val){
            return judgeSymmetric(left.left,right.right) && judgeSymmetric(left.right,right.left);
        }
        return false;
    }
}
