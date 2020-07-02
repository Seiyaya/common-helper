package xyz.seiyaya.leetcode.offer;

import java.util.LinkedList;

/**
 * 使用两个栈实现队列
 * 此处栈最好用linkedList
 * @author wangjia
 * @version 1.0
 */
public class Offer9 {

    public static void main(String[] args) {
        CQueue cQueue = new CQueue();
        cQueue.appendTail(3);
        int i = cQueue.deleteHead();
        System.out.println("delete head :" + i);
        i = cQueue.deleteHead();
        System.out.println("delete head :" + i);
    }


    private static class CQueue {

        LinkedList<Integer> stackA = new LinkedList<>();
        LinkedList<Integer> stackB = new LinkedList<>();

        public CQueue() {

        }

        public void appendTail(int value) {
            stackA.add(value);
        }

        public int deleteHead() {
            if(stackA.isEmpty()){
                return -1;
            }
            while(!stackA.isEmpty()){
                Integer pop = stackA.pop();
                stackB.add(pop);
            }
            Integer result = stackB.pop();
            while(!stackB.isEmpty()){
                Integer pop = stackB.pop();
                stackA.add(pop);
            }
            return result;
        }
    }
}
