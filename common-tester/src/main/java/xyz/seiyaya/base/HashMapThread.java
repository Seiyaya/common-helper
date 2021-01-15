package xyz.seiyaya.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/14 10:35
 */
@SuppressWarnings("all")
public class HashMapThread extends Thread {

    private static AtomicInteger ai = new AtomicInteger(0);

    private static Map<Integer,Integer> map = new HashMap<>(1);


    public void run(){
        while(ai.get() < 100000){
            map.put(ai.get(),ai.get());
            ai.incrementAndGet();
        }
    }


    public static void main(String[] args) {
        new HashMapThread().start();
        new HashMapThread().start();
        new HashMapThread().start();
        new HashMapThread().start();
        new HashMapThread().start();

        for(int i=1;i<5;i++){
            double tmp = (double)i/10 + 1;
            int count = 0;
            double sum = 1;
            while(sum < 2){
                sum = sum * tmp;
                count++;
            }
            System.out.printf("roe: %s  year:%s",i*10,count);
            System.out.println();
        }
    }
}
