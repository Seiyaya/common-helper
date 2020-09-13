package xyz.seiyaya.base;

import xyz.seiyaya.common.helper.TimeHelper;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/11 11:04
 */
public class ThreadVisibleDemo {

    private static boolean stop = false;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int count = 0;
            while (!stop) {
                TimeHelper.sleep(1);
                System.out.println(count++);
            }
        });


        Thread thread1 = new Thread(()->{
            stop = true;
            System.out.println("执行stop");
            TimeHelper.sleep(3);
        });

        thread.start();
        TimeHelper.sleep(1);
        thread1.start();

        thread.join();
        thread1.join();
    }
}
