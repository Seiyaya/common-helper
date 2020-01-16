package xyz.seiyaya.collection;

import org.junit.Test;

import java.util.ArrayList;
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
public class ArrayListTester {

    public static void main(String[] args) {
        String one = "1";
        Boolean b1 = Boolean.parseBoolean(one); // line n1
        Integer i1 = new Integer(one);
        Integer i2 = 1;
        Integer i3 = 2-1;
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
}
