package xyz.seiyaya.leetcode;

import lombok.ToString;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/28 9:06
 */
public class Solution116 {

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right = new Node(3);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        Node connect = new Solution116().connect2(root);
        System.out.println("result:"+ connect);
    }

    public Node connect2(Node root){
        if(root == null ){
            return null;
        }
        if(root.left != null){
            root.left.next = root.right;
        }
        if(root.right != null && root.next != null){
            root.right.next = root.next.left;
        }
        connect2(root.left);
        connect2(root.right);
        return root;
    }

    /**
     * 利用层序遍历解决
     * @param root
     * @return
     */
    public Node connect(Node root) {
        if(root == null){
            return null;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            int size = queue.size();
            Node last = null;
            for(int i=0;i<size;i++){
                Node poll = queue.poll();
                if(last != null){
                    last.next = poll;
                }
                last = poll;
                if(poll.left != null){
                    queue.add(poll.left);
                }
                if(poll.right != null){
                    queue.add(poll.right);
                }
            }
            last.next = null;
        }
        return root;
    }

    @ToString
    private static class Node{
        int val;
        Node left;
        Node right;
        Node next;

        public Node(int val){
            this.val = val;
        }

        public Node(int val,Node left,Node right ,Node next){
            this.val = val;
            this.left = left;
            this.right = right;
            this.next = next;
        }
    }
}
