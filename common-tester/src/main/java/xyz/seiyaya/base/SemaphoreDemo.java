package xyz.seiyaya.base;

import java.util.concurrent.Semaphore;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/7 9:58
 */
public class SemaphoreDemo {

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(10, true);


        // 每次获取一个，获取不到就会阻塞
        semaphore.acquire();

        semaphore.release();
    }
}
