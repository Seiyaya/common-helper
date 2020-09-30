package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.Node;

import java.util.LinkedList;
import java.util.Queue;

/**
 * n叉树的最大深度
 * @author wangjia
 * @version 1.0
 * @date 2020/9/27 18:16
 */
public class Solution559 {

    public static void main(String[] args) {
        Node root = new Node(1,new int[]{3,2,4});
        root.getNodeByValue(3).addChild(new int[]{5,6});
        int i = new Solution559().maxDepth(root);
        System.out.println("max dept:"+i);
    }

    public int maxDepth(Node root) {
        if(root == null){
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int dept = 0;
        while(!queue.isEmpty()){
            int size = queue.size();
            for(int i=0;i<size;i++){
                Node node = queue.poll();
                if(node != null && node.children != null){
                    queue.addAll(node.children);
                }
            }
            dept++;
        }
        return dept;
    }
}
