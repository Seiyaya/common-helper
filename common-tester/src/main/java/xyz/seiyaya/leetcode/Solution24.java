package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.ListNode;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/13 9:07
 */
public class Solution24 {

    public static void main(String[] args) {
        ListNode head = ListNode.init(new int[]{1,2,3,4});
        head = new Solution24().swapPairs2(head);
        ListNode.foreach(head);
    }

    /**
     * 递归解法
     * @param head
     * @return
     */
    public ListNode swapPairs2(ListNode head){
        if(head == null || head.next == null){
            return head;
        }
        ListNode next = head.next;
        head.next = swapPairs2(next.next);
        next.next = head;
        return next;
    }

    public ListNode swapPairs(ListNode head) {
        if(head == null || head.next == null){
            return head;
        }
        ListNode next = head.next;
        ListNode current = head;
        head = next;
        ListNode pre = null;
        while(next != null){
            if(pre != null){
                pre.next = next;
            }
            ListNode nextNext = next.next;
            current.next = nextNext;
            next.next = current;
            pre = current;
            current = nextNext;
            next = current == null ? null : current.next;
        }
        return head;
    }
}
