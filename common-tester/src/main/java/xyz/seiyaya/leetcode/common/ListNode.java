package xyz.seiyaya.leetcode.common;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/9 9:15
 */
public class ListNode {

    public int val;

    public ListNode next;

    public ListNode(int val){
        this.val = val;
    }

    public static ListNode init(int[] ints) {
        ListNode head = new ListNode(ints[0]);
        ListNode current = head;
        for(int i=1;i<ints.length;i++){
            current.next = new ListNode(ints[i]);
            current = current.next;
        }
        return head;
    }

    public static void foreach(ListNode head){
        while(head != null){
            System.out.print(head.val+" --> ");
            head = head.next;
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return "ListNode{" +
                "val=" + val +
                ", next=" + (next == null ? "null" : next.val) +
                '}';
    }
}
