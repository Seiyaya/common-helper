package xyz.seiyaya.base;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/26 9:35
 */
@SuppressWarnings("all")
public class SetFlagThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        SetFlagThread thread = new SetFlagThread();

        thread.start();
        TimeUnit.SECONDS.sleep(3);
        // 只有声明了抛出interrupt异常的才会抛出异常，正常情况下和加锁都不会
        thread.interrupt();

        System.out.println("main thread print sub thread :" +thread.isInterrupted());
        thread.stopThread();
        thread.join();
    }

    private static class SetFlagThread extends Thread{
        private boolean stop = false;

        @Override
        public void run() {
            while(!stop){
                try {
                    System.out.println("sub thread running");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("catch exception, current thread status : "+Thread.interrupted());
                    throw new RuntimeException(e.getCause());
                }
            }
        }

        public void stopThread(){
            stop = true;
        }
    }
}
