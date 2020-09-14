package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
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
        new Solution94().foreachIterable(root);
        System.out.println(integers);
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
        while(tmp != null ){
            while(tmp != null){
                stack.push(tmp);
                tmp = tmp.left;
            }
            while(!stack.isEmpty()){
                tmp = stack.pop();
                result.add(tmp.val);
                tmp = tmp.right;
            }
        }

        System.out.println("foreach result:"+result);
    }
}
