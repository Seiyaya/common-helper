package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/20 10:22
 */
public class Solution143 {

    public static void main(String[] args) {
        ListNode init = ListNode.init(new int[]{1,2});
        new Solution143().reorderList(init);

        ListNode.foreach(init);
    }

    public void reorderList(ListNode head) {
        List<ListNode> list = new ArrayList<>();
        ListNode tmp = head;
        while(tmp != null){
            list.add(tmp);
            tmp = tmp.next;
        }

        for(int i=0,j=list.size()-1;i<j;i++,j--){
            ListNode current = list.get(i);
            ListNode next = current.next;
            if(current.next == list.get(j)){
                break;
            }
            current.next = list.get(j);
            list.get(j).next = next;
            list.get(j-1).next = null;
        }
    }
}
