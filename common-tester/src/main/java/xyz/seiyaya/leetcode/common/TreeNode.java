package xyz.seiyaya.leetcode.common;

import java.util.LinkedList;
import java.util.Queue;

public class TreeNode {

    public int val;

    public TreeNode left;

    public TreeNode right;

    public TreeNode(int val){
        this.val = val;
    }


    public void foreachTreeNode() {
        TreeNode treeNode = this;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(treeNode);

        TreeNode root = treeNode;
        int count = 1;
        while(!queue.isEmpty()){
            int forCount = queue.size();
            System.out.println("level:" + count++);
            for(int i=0;i<forCount;i++){
                TreeNode poll = queue.poll();
                if(poll.left != null){
                    queue.add(poll.left);
                }
                if(poll.right != null){
                    queue.add(poll.right);
                }
                System.out.print(poll.val + "-->");
            }
            System.out.println();
        }
    }
}
