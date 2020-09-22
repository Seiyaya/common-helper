package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 给定一个二叉搜索树（Binary Search Tree），把它转换成为累加树（Greater Tree)，使得每个节点的值是原来的节点值加上所有大于它的节点值之和。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/convert-bst-to-greater-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author wangjia
 * @version 1.0
 * @date 2020/9/21 8:43
 */
public class Solution538 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(2);
        root.right = new TreeNode(13);
        TreeNode result = new Solution538().convertBST(root);
        result.foreachTreeNode();
    }

    private int num = 0;

    public TreeNode convertBST2(TreeNode root){
        if(root == null){
            return null;
        }

        convertBST(root.right);
        root.val = root.val + num;
        num = root.val;
        convertBST(root.left);
        return root;
    }

    public TreeNode convertBST(TreeNode root) {
        if(root == null){
            return null;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        List<TreeNode> trees = new ArrayList<>();
        while(!queue.isEmpty()){
            int countSize = queue.size();
            for(int i=0;i<countSize;i++){
                TreeNode poll = queue.poll();
                trees.add(poll);
                if(poll.left != null){
                    queue.add(poll.left);
                }
                if(poll.right != null){
                    queue.add(poll.right);
                }
            }
        }

        List<Integer> resultList = new ArrayList<>();
        for(int i=0;i<trees.size();i++){
            int tmpResult = trees.get(i).val;
            int currentResult = trees.get(i).val;
            for (TreeNode tree : trees) {
                if (currentResult < tree.val) {
                    tmpResult += tree.val;
                }
            }
            resultList.add(tmpResult);
        }

        for(int i=0;i< trees.size();i++){
            trees.get(i).val = resultList.get(i);
        }
        return root;
    }
}
