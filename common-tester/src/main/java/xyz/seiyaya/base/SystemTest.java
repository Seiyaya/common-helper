package xyz.seiyaya.base;

import org.junit.Test;

/**
 * @author wangjia
 * @date 2020/5/14 10:46
 */
public class SystemTest {

    @Test
    public void testSystemProperties(){
        System.setProperty("a","b");


        System.out.println(System.getProperty("a"));
    }

    @Test
    public void testNull(){
        Integer num = null;

        System.out.println(String.valueOf(num));
        System.out.println(String.valueOf(null));
    }
}
