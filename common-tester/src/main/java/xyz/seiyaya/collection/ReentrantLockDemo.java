package xyz.seiyaya.collection;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wangjia
 * @version v3.9.3
 * @date 2020/5/9 17:23
 */
@SuppressWarnings("all")
public class ReentrantLockDemo {

    private static final ReentrantLock reentrantLock = new ReentrantLock();

    @SuppressWarnings("all")
    public static void main(String[] args) throws InterruptedException {
        Condition condition = reentrantLock.newCondition();

        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("线程1加锁成功");
                System.out.println("线程1执行await方法");
                condition.await();
                System.out.println("线程1唤醒成功");
            } catch (Exception e) {

            } finally {
                reentrantLock.unlock();
                System.out.println("线程1释放锁成功");
            }
        }).start();

        TimeUnit.SECONDS.sleep(3);

        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("线程二加锁成功");
                condition.signal();
                System.out.println("线程二唤醒线程一");
            } finally {
                reentrantLock.unlock();
                System.out.println("线程二释放锁成功");
            }
        }).start();
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void testReadWriteLock() {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
    }


    private static int count = 0;

    @Test
    public void testPrintAbc() throws InterruptedException {
        Condition a = reentrantLock.newCondition();
        Condition b = reentrantLock.newCondition();
        Condition c = reentrantLock.newCondition();

        Thread aThread = new Thread(new PrintRunnable("A", a, b, 0));
        Thread bThread = new Thread(new PrintRunnable("B", b, c, 1));
        Thread cThread = new Thread(new PrintRunnable("C", c, a, 2));

        aThread.start();
        bThread.start();
        cThread.start();

        aThread.join();
        bThread.join();
        cThread.join();
    }

    private static class PrintRunnable implements Runnable {

        private String item;

        private Condition currentCondition;

        private Condition nextCondition;

        private int num;

        public PrintRunnable(String item, Condition currentCondition, Condition nextCondition, int num) {
            this.item = item;
            this.currentCondition = currentCondition;
            this.nextCondition = nextCondition;
            this.num = num;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                reentrantLock.lock();
                try {
                    while (count % 3 != num) {
                        currentCondition.await();
                    }
                    count++;
                    System.out.print(item);
                    nextCondition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    reentrantLock.unlock();
                }
            }
        }
    }
}
