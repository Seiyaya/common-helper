package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/4 16:06
 */
public class Solution257 {

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        treeNode.left = new TreeNode(2);
        treeNode.left.right = new TreeNode(5);
        treeNode.right = new TreeNode(3);

        List<String> strings = new Solution257().binaryTreePaths(treeNode);
        System.out.println(strings);
    }


    public List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        dfs(root,"",result);
        return result;
    }

    private void dfs(TreeNode root,String cur,List<String> list) {
        if(root == null){
            return;
        }
        cur += root.val;
        if(root.left == null && root.right == null){
            list.add(cur);
        }else{
            dfs(root.left,cur+"->", list);
            dfs(root.right, cur+"->" ,list);
        }
    }

    private static class TreeNode{
        int val;

        TreeNode left;
        TreeNode right;

        public TreeNode(int x){
            this.val = x;
        }
    }
}
