package xyz.seiyaya.base;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/10 10:40
 */
public class HashMapTest {

    @Test
    public void testKey(){
        Map<Person,String> map = new HashMap<>(16);
        Person person1 = new Person("abs");
        Person person2 = new Person("abs");
        map.put(person1,"lina");


        // 得到的是null  所以作为map对象的key必须要重写hashCode方法和equals方法
        System.out.println(map.get(person2));
    }
}
