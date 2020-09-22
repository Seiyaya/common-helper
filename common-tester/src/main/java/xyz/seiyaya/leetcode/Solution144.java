package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的前序遍历
 * @author wangjia
 * @version 1.0
 * @date 2020/9/22 17:33
 */
public class Solution144 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
        List<Integer> integers = new Solution144().preorderTraversal(root);
        System.out.println(integers);


        List<Integer> integers2 = new Solution144().preorderTraversalByIterable(root);
        System.out.printf("迭代解法:%s",integers2);
    }

    /**
     * 迭代解法
     * @param root
     * @return
     */
    private List<Integer> preorderTraversalByIterable(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> result = new ArrayList<>();
        while(root != null || !stack.isEmpty()){
            while(root != null){
                result.add(root.val);
                stack.add(root);
                root = root.left;
            }
            root = stack.pop().right;
        }
        return result;
    }

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if(root != null){
            dfs(root,result);
        }
        return result;
    }

    /**
     * 递归解法
     * @param root
     * @param result
     */
    private void dfs(TreeNode root, List<Integer> result) {
        if(root == null){
            return ;
        }
        result.add(root.val);
        dfs(root.left,result);
        dfs(root.right,result);
    }
}
