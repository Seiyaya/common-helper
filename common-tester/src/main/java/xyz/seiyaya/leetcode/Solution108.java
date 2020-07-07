package xyz.seiyaya.leetcode;

/**
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

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
