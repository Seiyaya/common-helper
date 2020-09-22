package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/16 9:12
 */
public class Solution226 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(1);

        root.right = new TreeNode(7);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(9);
        root.foreachTreeNode();
        TreeNode treeNode = new Solution226().invertTree(root);
        treeNode.foreachTreeNode();
    }

    public TreeNode invertTree(TreeNode root) {
        if(root == null){
            return null;
        }
        swap(root);
        return root;
    }

    private void swap(TreeNode root) {
        if(root == null){
            return ;
        }
        swap(root.left);
        swap(root.right);
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
    }
}
