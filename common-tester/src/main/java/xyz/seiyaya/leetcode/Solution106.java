package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * 从中序与后序遍历序列构造二叉树
 *
 * @author wangjia
 * @version 1.0
 * @date 2020/9/25 9:05
 */
@SuppressWarnings("all")
public class Solution106 {

    public static void main(String[] args) {
        TreeNode treeNode = new Solution106().buildTree(new int[]{9, 3, 15, 20, 7}, new int[]{9, 15, 7, 20, 3});
        treeNode.foreachTreeNode();
    }


    public TreeNode buildTree(int[] inorder, int[] postorder) {
        return build(inorder, 0, inorder.length - 1, postorder, 0, postorder.length - 1);
    }

    private TreeNode build(int[] inorder, int inStart, int inEnd, int[] postorder, int postStart, int postEnd) {
        if (inStart > inEnd || postStart > postEnd) {
            return null;
        }
        int root = postorder[postEnd];

        // 查找中序遍历划分左子树和右子树的节点
        int mid = inStart;
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder[i] == root) {
                mid = i;
                break;
            }
        }

        TreeNode rootNode = new TreeNode(root);
        // 左子树长度  mid - instart
        rootNode.left = build(inorder, inStart, mid - 1, postorder, postStart, postStart + mid - inStart - 1);
        rootNode.right = build(inorder, mid + 1, inEnd, postorder, postStart + mid - inStart, postStart + inEnd - inStart - 1);
        return rootNode;
    }
}
