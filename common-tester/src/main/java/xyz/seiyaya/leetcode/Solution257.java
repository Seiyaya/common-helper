package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个二叉树，返回所有从根节点到叶子节点的路径。
 * 输入:
 *
 *    1
 *  /   \
 * 2     3
 *  \
 *   5
 *
 * 输出: ["1->2->5", "1->3"]
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

    private void dfs(TreeNode root, String cur, List<String> list) {
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
}
