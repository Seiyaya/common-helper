package xyz.seiyaya.leetcode;

import lombok.ToString;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/28 9:33
 */
public class Solution117 {

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right = new Node(3);
        root.right.right = new Node(7);
        Node connect = new Solution117().connect(root);
        System.out.println(connect);
    }

    public Node connect(Node root) {
        if(root == null){
            return null;
        }
        if(root.left != null){
            if(root.right != null){
                root.left.next = root.right;
            }else{
                root.left.next = getNext(root.next);
            }
        }

        if(root.right != null){
           root.right.next = getNext(root.next);
        }
        connect(root.left);
        connect(root.right);
        return root;
    }

    /**
     * 获取next节点
     * @param root
     * @return
     */
    private Node getNext(Node root) {
        if(root == null){
            return null;
        }
        if(root.left != null){
            return root.left;
        }
        if(root.right != null){
            return root.right;
        }
        return getNext(root.next);
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

        public Node(int val, Node left, Node right , Node next){
            this.val = val;
            this.left = left;
            this.right = right;
            this.next = next;
        }
    }
}
