package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/6/16 8:45
 */
public class Solution297 {

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        treeNode.left = new TreeNode(2);
        treeNode.right = new TreeNode(3);
        treeNode.right.left = new TreeNode(4);
        treeNode.right.right = new TreeNode(5);

        Solution297 solution = new Solution297();

        String serialize = solution.serialize(treeNode);
        System.out.println("序列化之后:"+serialize);

        TreeNode deserialize = solution.deserialize(serialize);
        System.out.println("反序列化之后:"+serialize);
    }


    public String serialize(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        foreachTreeNode(root,list);
        System.out.println(list);
        return list.toString();
    }

    private void foreachTreeNode(TreeNode root,List<Integer> list) {
        list.add(root == null ? null : root.val);
        if(root == null){
            return ;
        }
        foreachTreeNode(root.left,list);
        foreachTreeNode(root.right,list);
    }

    public TreeNode deserialize(String data) {
        return null;
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
