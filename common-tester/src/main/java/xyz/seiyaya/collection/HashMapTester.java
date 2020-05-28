package xyz.seiyaya.collection;

import org.junit.Test;
import xyz.seiyaya.base.Person;

import java.util.HashMap;
import java.util.Hashtable;
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


    @Test
    public void testKey(){
        Map<Person,String> map = new HashMap<>(16);
        Person person1 = new Person("abs");
        Person person2 = new Person("abs");
        map.put(person1,"lina");


        // 得到的是null  所以作为map对象的key必须要重写hashCode方法和equals方法
        System.out.println(map.get(person2));
    }

    /**
     * HashTable采用的是用%取余数，而HashMap采用个是&取余数 <br/>
     * 所以table可以要求初始化的大小不为2的次幂，而map必须要求初始化大小为2次幂
     */
    @Test
    public void testHashTable(){
        Hashtable<String,Object> table =  new Hashtable<>(10);
        table.put("a","b");
    }
}
