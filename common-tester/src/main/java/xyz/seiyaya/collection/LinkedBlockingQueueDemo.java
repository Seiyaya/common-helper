package xyz.seiyaya.collection;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/8 10:08
 */
public class LinkedBlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

        queue.put(3);
    }
}
