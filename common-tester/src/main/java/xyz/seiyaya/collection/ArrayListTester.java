package xyz.seiyaya.collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ArrayList实现
 * @author wangjia
 * @version 1.0
 * @date 2019/11/4 17:29
 */
@SuppressWarnings("all")
@Slf4j
public class ArrayListTester {

    public static void main(String[] args) {
        String one = "1";
        Boolean b1 = Boolean.parseBoolean(one); // line n1
        Integer i1 = new Integer(one);
        Integer i2 = 1;
        Integer i3 = 2-1;
        Integer i4 = Integer.parseInt("3");
        System.out.println(i1 == i2);
        System.out.println(i2 == i3);
        System.out.println(i3 == i1);
    }

    @Test
    public void testConcurrencyAddElement() throws InterruptedException {
        // 添加元素引起的并发问题
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10,20,1, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<100;i++){
            int finalI = i;
            executor.execute(() -> {
                list.add(finalI);
            });
        }

        TimeUnit.SECONDS.sleep(2);

        for(int i=0;i<100;i++){
            System.out.println(i + ":--> " + list.get(i));
        }

        System.out.println("list size:"+list.size());
    }

    @Test
    public void testStr(){
        String str1 = "12";
        String str2 = new String("12");
        String str3 = "1"+new String("2");
        String str4 = new StringBuilder("1").append("2").toString().intern();
        System.out.println(str1 == str2);

        System.out.println(str1 == str3);

        System.out.println(str2 == str4);

        System.out.println(str1 == str4);
    }

    @Test
    public void testArraySort(){
        int[] array = new int[]{3,1,2};
        Arrays.sort(array);
        log.info("array sort result:{}",array);

        List<Integer> intList = Arrays.asList(3, 2, 1);
        Collections.sort(intList);
        log.info("list:{}",intList);
    }

    @Test
    public void testAdd(){
        List<String> list = new ArrayList<>();
        list.add("123");

        list.add(null);

        list.add("456");

        System.out.println(list);
    }
}
