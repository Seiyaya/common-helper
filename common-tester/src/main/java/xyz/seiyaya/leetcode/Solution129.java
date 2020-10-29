package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * 给定一个二叉树，它的每个结点都存放一个 0-9 的数字，每条从根到叶子节点的路径都代表一个数字
 */
public class Solution129 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(9);
        root.right = new TreeNode(0);
        root.left.left = new TreeNode(5);
        root.left.right = new TreeNode(1);


        int i = new Solution129().sumNumbers(root);
        System.out.println("result:"+i);
    }

    private int result = 0;

    public int sumNumbers(TreeNode root) {
        StringBuilder stringBuilder = new StringBuilder();

        dfs(root,0);

        return result;
    }

    public void dfs(TreeNode root,int num){
        if(root == null){
            return ;
        }
        if(root.left == null && root.right == null){
            result += 10 * num +root.val;
        }
        dfs(root.left,10 * num + root.val);
        dfs(root.right,10 * num + root.val);
    }
}
