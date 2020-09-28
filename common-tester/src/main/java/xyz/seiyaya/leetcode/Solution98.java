package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * 验证二叉树
 * @author wangjia
 * @version 1.0
 * @date 2020/9/24 10:57
 */
public class Solution98 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(1);
        boolean validBST = new Solution98().isValidBST(root);
        System.out.println(validBST);
    }

    private double pre = Double.MIN_VALUE;

    /**
     * 验证二叉查找树只需要中序遍历前驱节点小于后继节点即可
     * @param root
     * @return
     */
    public boolean isValidBST(TreeNode root) {
        return dfs(root);
    }

    private boolean dfs(TreeNode root) {
        if(root == null){
            return true;
        }
        if(dfs(root.left)){
            if(pre < root.val){
                pre = root.val;
                return dfs(root.right);
            }
        }
        return false;
    }
}
