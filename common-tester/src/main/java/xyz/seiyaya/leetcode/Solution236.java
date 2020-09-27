package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * 二叉树的公共祖先
 * @author wangjia
 * @version 1.0
 * @date 2020/9/27 14:18
 */
public class Solution236 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(5);
        root.left.left = new TreeNode(6);
        root.left.right = new TreeNode(2);
        root.left.right.left = new TreeNode(7);
        root.left.right.right = new TreeNode(4);

        root.right = new TreeNode(1);
        root.right.left = new TreeNode(0);
        root.right.right = new TreeNode(8);
        TreeNode treeNode = new Solution236().lowestCommonAncestor(root, new TreeNode(2), new TreeNode(8));
        System.out.println("公共祖先:"+treeNode.val);
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null || root.val == p.val || root.val == q.val){
            return root;
        }
        // 遍历左子树
        TreeNode left = lowestCommonAncestor(root.left,p,q);
        // 遍历右子树
        TreeNode right = lowestCommonAncestor(root.right,p,q);
        if(left == null && right == null){
            // 左子树和右子树都不包含p或者q
            return null;
        }
        // 左子树没有找到p或者q
        if(left == null){
            return right;
        }
        // 右子树没有找到p或者q
        if(right == null){
            return left;
        }
        // p或者q分别在树的两侧
        return root;
    }
}
