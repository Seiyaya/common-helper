package xyz.seiyaya;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.util.MathArrays;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import xyz.seiyaya.common.helper.DBParam;
import xyz.seiyaya.common.helper.HttpHelper;
import xyz.seiyaya.service.ParentTwo;

import java.math.BigDecimal;
import java.net.URISyntaxException;
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
    public void sendHttps() throws URISyntaxException {
        String s = new HttpHelper().sendGet("https://www.jianshu.com/shakespeare/v2/notes/3d8fd2c29cb7/audio");

        System.out.println(s);
    }
}
