package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/23 10:48
 */
@SuppressWarnings("all")
public class Solution103 {

    public static void main(String[] args) {
        List<List<Integer>> lists = new Solution103().zigzagLevelOrder(TreeNode.createDefaultTreeNode());
        System.out.println(lists);
    }

    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if(root == null){
            return result;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int level = 1;
        while(!queue.isEmpty()){
            int size = queue.size();
            List<Integer> innerResult = new ArrayList<>(size);
            // true表示从右往左  false表示从左往右
            boolean flag = level % 2 == 0;
            for(int i=0;i<size;i++){
                TreeNode poll = queue.poll();
                if(poll.left != null){
                    queue.add(poll.left);
                }
                if(poll.right != null){
                    queue.add(poll.right);
                }
                innerResult.add(poll.val);
            }
            if(flag){
                int start = 0;
                int end = size-1;
                while(start < end){
                    Integer tmp = innerResult.get(start);
                    innerResult.set(start,innerResult.get(end));
                    innerResult.set(end,tmp);
                    start++;
                    end--;
                }
            }
            result.add(innerResult);
            level++;
        }
        return result;
    }
}
