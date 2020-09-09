package xyz.seiyaya.collection;

import org.junit.Test;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/8 17:08
 */
public class ConcurrentSkipListMapDemo {

    @Test
    public void testSkipList(){
        ConcurrentSkipListMap<Integer, Integer> map = new ConcurrentSkipListMap<>();
        map.put(1,1);
        map.put(2,1);
    }
}
