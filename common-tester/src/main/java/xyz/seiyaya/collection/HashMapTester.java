package xyz.seiyaya.collection;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/31 15:57
 */
public class HashMapTester {

    public static void main(String[] args) {
        int size = 64;
        Map<HashA,Integer> maps = new HashMap<>(size);
        HashA one = new HashA(0);
        HashA two = new HashA(size);
        System.out.println(String.format("one-->%s  two-->%s",one.hashCode(),two.hashCode()));
        maps.put(one,one.hashCode());
        maps.put(two,two.hashCode());
        System.out.println(maps.size());
        maps.forEach((k,v)->{
            System.out.println(k+"-->"+v);
        });
        for(int i=2;i<9;i++){
            maps.put(new HashA(i*size),i*size);
        }

        Integer a = maps.get(one);
        System.out.println(a);
        System.out.println((3 << 4));

    }

    /**
     * 用来验证hashMap是线程不安全的
     * 因为最后的map可能只有一个元素，第二个元素会覆盖第一个元素
     * @throws InterruptedException
     */
    @Test
    public void insertError() throws InterruptedException {
        for(int j=0;j<100;j++){
            int size = 64;
            Map<HashA,Integer> maps = new HashMap<>(size);
            for(int i=0;i<2;i++){
                int finalI = i;
                new Thread(() -> {
                    HashA one = new HashA(finalI *size);
                    maps.put(one,finalI *size);
                }).start();
            }

            TimeUnit.SECONDS.sleep(1);
            System.out.println(maps);
        }
    }
}
