package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的中序遍历
 * @author wangjia
 * @version 1.0
 * @date 2020/9/14 8:48
 */
public class Solution94 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(3);
        List<Integer> integers = new Solution94().inorderTraversal(root);
        System.out.printf("递归结果:%s \n",integers);
        new Solution94().foreachIterable(root);
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        foreachTree(root,result);
        return result;
    }

    private void foreachTree(TreeNode root, List<Integer> result) {
        if(root == null){
            return;
        }
        foreachTree(root.left,result);
        result.add(root.val);
        foreachTree(root.right,result);
    }

    private void foreachIterable(TreeNode root){
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> result = new ArrayList<>();

        TreeNode tmp = root;
        while(tmp != null || !stack.isEmpty()){
            while(tmp != null){
                stack.add(tmp);
                tmp = tmp.left;
            }
            TreeNode pop = stack.pop();
            result.add(pop.val);
            tmp = pop.right;
        }

        System.out.println("迭代结果:"+result);
    }
}
