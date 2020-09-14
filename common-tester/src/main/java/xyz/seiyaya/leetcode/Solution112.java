package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * 给定一个二叉树和一个目标和，判断该树中是否存在根节点到叶子节点的路径，这条路径上所有节点值相加等于目标和。
 * 给定如下二叉树，以及目标和 sum = 22，
 * @author wangjia
 * @version 1.0
 * @date 2020/7/7 8:39
 */
public class Solution112 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.left.left = new TreeNode(11);
        root.left.left.left = new TreeNode(7);
        root.left.left.right = new TreeNode(2);

        root.right = new TreeNode(8);
        root.right.left = new TreeNode(13);
        root.right.right = new TreeNode(4);
        root.right.right.right = new TreeNode(1);
        boolean b = new Solution112().hasPathSum(root, 22);
        System.out.println("result:" + b);
    }

    public boolean hasPathSum(TreeNode root, int sum) {
        if(getPath(root,sum)){
            return true;
        }
        return false;
    }

    private boolean getPath(TreeNode root, int sum) {
        if(root == null){
            return false;
        }
        if(root.left == null && root.right == null){
            return sum - root.val == 0;
        }
        return getPath(root.left,sum-root.val) || getPath(root.right,sum-root.val);
    }
}
