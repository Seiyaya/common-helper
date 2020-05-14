package xyz.seiyaya.collection;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangjia
 * @date 2020/5/11 17:01
 */
public class AQSDemo {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock reentrantLock = new ReentrantLock();
        Object obj = new Object();
        new Thread(() -> {

            reentrantLock.lock();
            try {
                synchronized (obj) {
                    obj.wait();
                    System.out.println("obj唤醒后:");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }).start();

        TimeUnit.SECONDS.sleep(3);

        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("线程B释放");
            } finally {
                reentrantLock.unlock();
            }
        }).start();

        TimeUnit.SECONDS.sleep(10);
        System.out.println("10s后唤醒obj");
        synchronized (obj) {
            obj.notify();
        }

        Thread.currentThread().join();
    }
}
