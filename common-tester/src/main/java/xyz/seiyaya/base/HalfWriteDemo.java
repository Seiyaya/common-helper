package xyz.seiyaya.base;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/26 16:15
 */
@SuppressWarnings("all")
public class HalfWriteDemo {

    private long a = 0;

    public long getA() {
        return a;
    }

    public void setA(long a) {
        this.a = a;
    }

    public static void main(String[] args) throws InterruptedException {
        HalfWriteDemo halfWriteDemo = new HalfWriteDemo();
        Thread thread1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long a = halfWriteDemo.getA();
            System.out.println("thread a : a= "+a);
        });

        Thread thread2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            halfWriteDemo.setA(100);
        });

        thread2.start();
        thread1.start();

        TimeUnit.SECONDS.sleep(1);
    }
}
