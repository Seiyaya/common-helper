package xyz.seiyaya.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/26 16:00
 */
public class ConsumerAndProviderDemo {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        Provider provider = new Provider(list);
        Consumer consumer = new Consumer(list);

        provider.start();
        consumer.start();

        provider.join();
        consumer.join();
    }

    private static class Consumer extends Thread {

        private List<Integer> list;

        public Consumer(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (list) {

                    try {
                        if (list.size() == 10) {
                            list.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 10; i++) {
                        int num = (int) (Math.random() * 1000);
                        list.add(num);
                        System.out.println("provide num " + num);
                    }

                    System.out.println("唤醒消费者进行消费");
                    list.notify();
                }
            }
        }
    }

    private static class Provider extends Thread {

        private List<Integer> list;

        public Provider(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (list) {

                    if (list.isEmpty()) {
                        try {
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Iterator<Integer> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Integer next = iterator.next();
                        System.out.println("consume num " + next);
                        iterator.remove();
                    }

                    System.out.println("唤醒生产者进行生产");
                    list.notify();
                }
            }
        }
    }
}
