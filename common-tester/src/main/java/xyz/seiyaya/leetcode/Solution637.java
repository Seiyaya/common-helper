package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution637 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        List<Double> doubles = new Solution637().averageOfLevels(root);
        System.out.println(doubles);
    }

    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> result = new ArrayList<>();
        if(root == null){
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while(!queue.isEmpty()){
            int len = queue.size();
            double sum = 0;
            // 遍历每一层
            for(int i=0;i<len;i++){
                TreeNode item = queue.poll();
                sum += item.val;
                if(item.left != null){
                    queue.add(item.left);
                }
                if(item.right != null){
                    queue.add(item.right);
                }
            }
            result.add(sum/len);
        }
        return result;
    }
}
