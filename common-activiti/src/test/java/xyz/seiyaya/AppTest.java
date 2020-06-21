package xyz.seiyaya;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        int i = new BigDecimal("123.12").intValue();
        System.out.println(i);
    }
}
