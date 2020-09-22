package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/22 18:01
 */
public class Solution145 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(3);
        List<Integer> integers = new Solution145().postorderTraversal(root);
        System.out.println("递归解法:"+integers);
    }

    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        dfs(root,result);
        return result;
    }

    private void dfs(TreeNode root, List<Integer> result) {
        if(root == null){
            return ;
        }
        dfs(root.left,result);
        dfs(root.right,result);
        result.add(root.val);
    }
}
