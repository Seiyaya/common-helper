package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/29 18:11
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
        dfs(root,0);
        return result;
    }

    /**
     * 本层该做什么   叶子节点的累加到result
     * 本层为上层(下层)提供什么   提供之前累加的数
     * 终止条件 root == null
     * @param root
     * @param sb
     */
    private void dfs(TreeNode root, int sb) {
        if( root == null){
            return;
        }
        if(root.left == null && root.right == null){
            System.out.println(sb * 10 + root.val);
            result = result + sb * 10 + root.val;
        }
        dfs(root.left, sb * 10 + root.val);
        dfs(root.right, sb * 10 + root.val);
    }
}
