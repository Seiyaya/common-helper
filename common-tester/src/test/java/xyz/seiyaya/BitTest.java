package xyz.seiyaya;

import org.junit.Test;
import xyz.seiyaya.collection.HashA;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangjia
 * @date 2020/5/26 15:56
 */
public class BitTest {

    @Test
    public void testBitCalc(){
        System.out.println(1 << 14);


        // 27 & 3 = 3
        System.out.println(27 & (4-1));
    }

    @Test
    public void testHashMap(){
        Set<HashA> set = new HashSet<>();
        set.add(new HashA(12));
        set.add(new HashA(24));
        set.add(new HashA(36));
        set.add(new HashA(102));

        System.out.println(set);
    }

    @Test
    public void testBit(){
        System.out.println(1 | 3);
    }
}
