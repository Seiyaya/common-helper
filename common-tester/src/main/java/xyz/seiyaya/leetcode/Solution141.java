package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.ListNode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/9 9:15
 */
public class Solution141 {

    public static void main(String[] args) {
        ListNode list = new ListNode(3);
        list.next = new ListNode(2);
        list.next.next = new ListNode(0);
        list.next.next.next = new ListNode(-4);
//        list.next.next.next.next = list.next;
        boolean b = new Solution141().hasCycle(list);

        System.out.println("hash cycle :"+b);
    }

    public boolean hasCycle(ListNode head) {
        if(head == null || head.next == null){
            return false;
        }
        ListNode fast = head;
        ListNode slow = head.next.next;
        while(fast != null && slow != null){
            if(fast.val == slow.val){
                return true;
            }
            fast = fast.next;
            slow = slow.next == null ? null : slow.next.next;
        }
        return false;
    }
}
