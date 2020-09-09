package xyz.seiyaya.collection;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/8 16:22
 */
public class ConcurrentHashMapDemo {

    @Test
    public void testConcurrentHashMap(){
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        map.put(1,2);
    }
}
