package xyz.seiyaya;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import xyz.seiyaya.common.helper.DBParam;

import java.util.HashMap;

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
}
