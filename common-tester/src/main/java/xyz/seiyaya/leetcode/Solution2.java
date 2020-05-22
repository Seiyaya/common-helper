package xyz.seiyaya.leetcode;

/**
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * @author wangjia
 * @date 2020/5/22 10:49
 */
public class Solution2 {

    public static void main(String[] args) {
        ListNode l1 = new ListNode(5);
        ListNode l2 = new ListNode(0);
        ListNode listNode = new Solution2().addTwoNumbers(l1, l2);
        while(listNode != null){
            System.out.print(listNode.val);
            listNode = listNode.next;
        }
        System.out.println();
    }

    /**
     * 先计算，在格式化
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode l1Head = l1;
        ListNode l1Last = null;
        while(l1 != null && l2!= null ){
            int tmp = l1.val + l2.val;
            l1.val = tmp;
            l1Last = l1;
            l1 = l1.next;
            l2 = l2.next;
        }
        if(l1 == null){
            // 直接将l2后面的拼接到l1
            l1Last.next = l2;
        }
        l1 = l1Head;
        while(l1 != null){
            if(l1.val >= 10){
                l1.val = l1.val - 10;
                if(l1.next == null){
                    l1.next = new ListNode(1);
                }else{
                    l1.next.val = l1.next.val+1;
                }
            }
            l1 = l1.next;
        }
        return l1Head;
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
