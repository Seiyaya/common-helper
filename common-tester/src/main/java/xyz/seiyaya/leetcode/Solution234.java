package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/23 9:07
 */
public class Solution234 {

    public static void main(String[] args) {
        ListNode init = ListNode.init(new int[]{1, 2, 2, 1});
        System.out.println(new Solution234().isPalindrome(init));
    }

    public boolean isPalindrome(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while(head != null){
            list.add(head.val);
            head = head.next;
        }
        int left = 0;
        int right = list.size()-1;
        while(left < right){
            if(!list.get(left).equals(list.get(right))){
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
