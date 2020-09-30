package xyz.seiyaya.leetcode.offer;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/28 16:48
 */
public class Offer55 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        root.right.left.right = new TreeNode(8);
        boolean balanced = new Offer55().isBalanced(root);
        System.out.println("result:"+balanced);
    }

    public boolean isBalanced(TreeNode root) {
        if(root == null){
            return true;
        }
        if(isBalanced(root.left) && isBalanced(root.right)){
            return true;
        }
        if(Math.abs(dfs(root.left) - dfs(root.right)) <=1){
            return true;
        }
        return false;
    }

    private int dfs(TreeNode root) {
        if(root == null){
            return 0;
        }
        return Math.max(dfs(root.left)+1,dfs(root.right)+1);
    }
}
