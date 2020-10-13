package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/12 9:05
 */
public class Solution530 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(2);
        int minimumDifference = new Solution530().getMinimumDifference2(root);
        System.out.println("min value :" +minimumDifference);
    }

    private int ans = Integer.MAX_VALUE;

    private TreeNode preNode;

    public int getMinimumDifference2(TreeNode root) {
        dfs(root);
        return ans;
    }

    private void dfs(TreeNode root) {
        if(root == null){
            return ;
        }
        dfs(root.left);
        if(preNode != null){
            ans = Math.min(ans,Math.abs(root.val - preNode.val));
        }
        preNode = root;
        dfs(root.right);
    }

    public int getMinimumDifference(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        List<Integer> list = new ArrayList<>();
        while(!queue.isEmpty()){
            int size = queue.size();
            for(int i=0;i<size;i++){
                TreeNode poll = queue.poll();
                list.add(poll.val);
                if(poll.left != null){
                    queue.add(poll.left);
                }
                if(poll.right != null){
                    queue.add(poll.right);
                }
            }
        }

        int result = Integer.MAX_VALUE;
        for(int i=0;i<list.size();i++){
            for(int j=i+1;j<list.size();j++){
                result = Math.min(result, Math.abs(list.get(i)-list.get(j)));
            }
        }

        return result;
    }
}
