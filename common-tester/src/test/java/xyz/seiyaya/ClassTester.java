package xyz.seiyaya;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import xyz.seiyaya.common.helper.DBParam;
import xyz.seiyaya.service.ParentTwo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * class相关方法测试
 * @author wangjia
 * @version 1.0
 * @date 2019/10/29 16:33
 */
public class ClassTester {

    /**
     *  判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口
     */
    @Test
    public void testIsAssignableFrom(){
        HashMap<String,Object> a = new HashMap<>();
        DBParam dbParam = new DBParam();
        System.out.println(a.getClass().isAssignableFrom(dbParam.getClass()));
        StandardDeviation standardDeviation = new StandardDeviation();
        double evaluate = standardDeviation.evaluate(new double[]{1, 1, 1, 1});
        System.out.println(evaluate);
    }

    @Test
    public void testInteger(){
        Integer i = Integer.valueOf("25");
        System.out.println( i == 25);
    }

    @Test
    public void testCopyBean(){
        Parent parent = new Son("myName",12,"001");
        Son son = new Son();
        BeanUtils.copyProperties(parent,son);

        System.out.println(son.getName() + "-->" + son.getStudentNo());
    }
    @Test
    public void testCopyBean2(){
        Parent parent = new Parent("myName",null);
        ParentTwo parentTwo = new ParentTwo();
        parentTwo.setAge(12);
        BeanUtils.copyProperties(parent,parentTwo);

        System.out.println(parentTwo);
    }

    @Test
    public void testMath(){
        BigDecimal a = new BigDecimal("8866128975287528").pow(3);
        BigDecimal b = new BigDecimal("-8778405442862239").pow(3);
        BigDecimal c = new BigDecimal("-2736111468807040").pow(3);
        System.out.println(a.add(b).add(c));
    }

    @Test
    public void testMaxInteger(){
        System.out.println(Integer.MAX_VALUE);
    }
    
    
    @Test
    public void testStreamSort(){
        List<Integer> integers = Arrays.asList(1, 2, 3);
        integers.sort(Comparator.comparing(new Function<Integer, Integer>() {

            @Override
            public Integer apply(Integer integer) {
                System.out.println(integer);
                return integer;
            }
        }));
    }
}
