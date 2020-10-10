package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.ListNode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/10 9:30
 */
public class Solution142 {

    public static void main(String[] args) {
        int[] data = new int[]{-21,10,17,8,4,26,5,35,33,-7,-16,27,-12,6,29,-12,5,9,20,14,14,2,13,-24,21,23,-21,5};
        ListNode head = new ListNode(data[0]);
        ListNode tmp = head;
        int pos = 24;
        ListNode flagNode = null;
        for(int i=1;i<data.length;i++){
            tmp.next = new ListNode(data[i]);
            tmp = tmp.next;
            if(i == pos){
                flagNode = tmp;
            }
        }

        tmp.next = flagNode;
        ListNode b = new Solution142().detectCycle(head);

        System.out.println("hash cycle :"+ (b == null ? null : b.val));
    }

    public ListNode detectCycle(ListNode head) {
        if(head == null || head.next == null){
            return null;
        }
        ListNode fast = head;
        ListNode slow = head;
        while(fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
            if(fast == slow){
                ListNode tmp = head;
                while(tmp != slow){
                    tmp = tmp.next;
                    slow = slow.next;
                }
                return slow;
            }
        }
        return null;
    }
}
