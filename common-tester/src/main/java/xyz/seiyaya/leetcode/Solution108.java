package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * 将一个按照升序排列的有序数组，转换为一棵高度平衡二叉搜索树。
 *
 * 本题中，一个高度平衡二叉树是指一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1。
 * @author wangjia
 * @version 1.0
 * @date 2020/7/3 8:33
 */
public class Solution108 {

    public static void main(String[] args) {
        TreeNode treeNode = new Solution108().sortedArrayToBST(new int[]{-10, -3, 0, 5, 9});
        System.out.println(treeNode);
    }

    public TreeNode sortedArrayToBST(int[] nums) {
        return newTreeNode(nums,0,nums.length-1);
    }

    private TreeNode newTreeNode(int[] nums, int leftIndex,int rightIndex) {
        if(leftIndex > rightIndex){
            return null;
        }
        int mid = leftIndex + (rightIndex-leftIndex)/2;
        TreeNode treeNode = new TreeNode(nums[mid]);
        treeNode.left = newTreeNode(nums,leftIndex,mid-1);
        treeNode.right = newTreeNode(nums,mid+1,rightIndex);
        return treeNode;
    }
}
