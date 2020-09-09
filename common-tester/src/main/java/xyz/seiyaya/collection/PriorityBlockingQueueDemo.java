package xyz.seiyaya.collection;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/8 10:15
 */
public class PriorityBlockingQueueDemo {

    public static void main(String[] args) {
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();

        queue.put(2);
        queue.put(1);
        queue.put(3);

        System.out.println(queue);
    }
}
