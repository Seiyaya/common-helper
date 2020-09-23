package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * merge两个二叉树
 * @author wangjia
 * @version 1.0
 * @date 2020/9/23 8:40
 */
public class Solution617 {

    public static void main(String[] args) {
        TreeNode t1 = new TreeNode(1);
        t1.left = new TreeNode(3);
        t1.right = new TreeNode(2);
//        t1.left.left = new TreeNode(5);

        TreeNode t2 = new TreeNode(2);
        t2.left = new TreeNode(1);
        t2.right = new TreeNode(3);
        t2.left.left = new TreeNode(3);
        t2.left.right = new TreeNode(4);
        t2.right.right = new TreeNode(7);
        TreeNode treeNode = new Solution617().mergeTrees(t1, t2);
        treeNode.foreachTreeNode();
    }

    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if(t1 == null){
            return t2;
        }
        if(t2 == null){
            return t1;
        }
        dfs(t1,t2);
        return t1;
    }

    private TreeNode dfs(TreeNode t1, TreeNode t2) {
        if(t1 == null && t2 == null){
            return null;
        }
        if(t1 == null){
            return t2;
        }
        t1.left = dfs(t1.left,t2 != null ? t2.left : null);
        t1.right = dfs(t1.right,t2 != null ? t2.right : null);
        if(t2 != null){
            t1.val += t2.val;
        }
        return t1;
    }
}
