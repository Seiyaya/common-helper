package xyz.seiyaya.base;

import org.junit.Test;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/9 13:47
 */
public class RuntimeDemo {

    @Test
    public void testAvailableProcessors(){
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println(i);
    }
}
