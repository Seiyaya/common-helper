package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class Solution113 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.left.left = new TreeNode(11);
        root.left.left.left = new TreeNode(7);
        root.left.left.right = new TreeNode(2);
        root.right = new TreeNode(8);
        root.right.left = new TreeNode(13);
        root.right.right = new TreeNode(4);
        root.right.right.left = new TreeNode(5);
        root.right.right.right = new TreeNode(1);
        new Solution113().pathSum(root,22);
    }

    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> result = new ArrayList<>();

        dfs(root,sum,result);

        return result;
    }

    private void dfs(TreeNode root, int sum, List<List<Integer>> result) {

    }
}
