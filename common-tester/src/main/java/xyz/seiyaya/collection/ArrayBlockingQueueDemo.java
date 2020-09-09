package xyz.seiyaya.collection;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/8 9:57
 */
public class ArrayBlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(3, false);
        queue.put(3);
    }
}
