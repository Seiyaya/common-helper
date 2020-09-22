package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * 给定一个二叉树，我们在树的节点上安装摄像头。
 *
 * 节点上的每个摄影头都可以监视其父对象、自身及其直接子对象。
 *
 * 计算监控树的所有节点所需的最小摄像头数量。
 * @author wangjia
 * @version 1.0
 * @date 2020/9/22 8:47
 */
public class Solution968 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(0);
        root.left.left = new TreeNode(0);
        root.left.right = new TreeNode(0);
        int resultI = new Solution968().minCameraCover(root);
        System.out.println(resultI);
    }

    private int result = 0;

    public int minCameraCover(TreeNode root) {
        if(root == null){
            return 0;
        }

        if(dfs(root) == 2){
            result++;
        }
        return result;
    }

    /**
     * 分为三种情况
     * 1. 该节点安装了监视器
     * 2. 该节点可观，单没有安装监视器
     * 3. 该节点不可观
     * @link https://leetcode-cn.com/problems/binary-tree-cameras/comments/14208
     * @param root
     * @return
     */
    private int dfs(TreeNode root) {
        if(root == null){
            return 1;
        }
        int left = dfs(root.left);
        int right = dfs(root.right);
        if(left == 2 || right == 2){
            result++;
            return 0;
        }else if(left == 0 || right == 0){
            return 1;
        }else{
            return 2;
        }
    }
}
