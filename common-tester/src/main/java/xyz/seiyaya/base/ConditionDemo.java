package xyz.seiyaya.base;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/3 17:12
 */
public class ConditionDemo {

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        queue.put(1);
        queue.take();
    }
}
