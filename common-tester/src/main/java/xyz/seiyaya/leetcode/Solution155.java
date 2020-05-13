package xyz.seiyaya.leetcode;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Stack;

/**
 * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
 * <p>
 * push(x) —— 将元素 x 推入栈中。
 * pop() —— 删除栈顶的元素。
 * top() —— 获取栈顶元素。
 * getMin() —— 检索栈中的最小元素。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/min-stack
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author wangjia
 * @date 2020/5/12 8:44
 */
public class Solution155 {


    public static void main(String[] args) {
        MinStack1 stack = new MinStack1();
        stack.push(2147483646);
        stack.push(2147483646);
        stack.push(2147483647);
        System.out.println("top:"+stack.top());
        stack.pop();
        System.out.println("min:" + stack.getMin());
        stack.pop();
        System.out.println("min:" + stack.getMin());
        stack.pop();
        stack.push(2147483647);
        System.out.println("top:"+stack.top());
        System.out.println("min:" + stack.getMin());
        stack.push(-2147483648);
        System.out.println("top:"+stack.top());
        System.out.println("min:" + stack.getMin());
        stack.pop();
        System.out.println("min:" + stack.getMin());
    }

    static class MinStack {

        private LinkedList<Integer> list = new LinkedList<>();

        public MinStack() {

        }

        public void push(int x) {
            list.add(x);
        }

        public void pop() {
            list.removeLast();
        }

        public int top() {
            return list.getLast();
        }

        public int getMin() {
            Optional<Integer> min = list.stream().min(Comparator.comparingInt(Integer::intValue));
            return min.orElse(0);
        }
    }


    static class MinStack1 {

        private Node head;

        private Node tail;

        public MinStack1() {
            new Stack<>().pop();
        }

        public void push(int x) {
            if (head == null) {
                head = new Node(x);
                tail = head;
            } else {
                Node node = new Node(x);
                tail.next = node;
                node.prev = tail;
                tail = node;
            }
        }

        public void pop() {
            tail = tail.prev;
            if(tail != null){
                tail.next = null;
            }else{
                head = null;
                tail = null;
            }
        }

        public int top() {
            return tail.val;
        }

        public int getMin() {
            Node forTmp = head;
            int min = Integer.MAX_VALUE;
            while (forTmp != null) {
                min = Math.min(forTmp.val, min);
                forTmp = forTmp.next;
            }
            return min;
        }
    }

    static class Node {
        private int val;
        private Node next;
        private Node prev;

        public Node(int val) {
            this.val = val;
        }
    }
}
