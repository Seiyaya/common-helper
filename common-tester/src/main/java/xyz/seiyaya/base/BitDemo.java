package xyz.seiyaya.base;

import org.junit.Test;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/8 15:52
 */
public class BitDemo {

    @Test
    public void testBit(){
        System.out.println(1 << 16);

        System.out.println("2 & -2 --> "  + (2 & -2));
    }
}
