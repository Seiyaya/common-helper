package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的后序遍历
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

        List<Integer> integers1 = new Solution145().postorderTraversalByIterable(root);
        System.out.println("迭代解法:"+integers1);
    }

    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        dfs(root,result);
        return result;
    }

    /**
     * 逆过程迭代
     * @param root
     * @return
     */
    public List<Integer> postorderTraversalByIterable(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();

        stack.add(root);
        while(!stack.isEmpty()){
            TreeNode pop = stack.pop();
            if(pop.left != null){
                stack.add(pop.left);
            }
            if(pop.right != null){
                stack.add(pop.right);
            }
            result.add(0,pop.val);
        }
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
