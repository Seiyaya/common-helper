package xyz.seiyaya.collection;

import java.util.concurrent.SynchronousQueue;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/8 13:51
 */
public class SynchronousQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> queue = new SynchronousQueue<>(false);
        queue.put(1);
        Integer take1 = queue.take();
        System.out.println("take1:"+take1);
        queue.put(2);
        take1 = queue.take();
        System.out.println("take1:"+take1);

        Integer peek = queue.peek();
        System.out.println(peek);

        Integer take = queue.take();
        System.out.println("take:"+take);
    }
}
