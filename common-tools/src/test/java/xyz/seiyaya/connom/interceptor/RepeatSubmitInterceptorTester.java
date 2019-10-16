package xyz.seiyaya.connom.interceptor;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.seiyaya.common.annotation.RepeatSubmitLimit;
import xyz.seiyaya.common.bean.ResultBean;

import java.lang.reflect.Method;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/16 9:44
 */
public class RepeatSubmitInterceptorTester {

    @Test
    public void testMethod() throws Exception {
        Method itemMethod = RepeatSubmitInterceptorTester.class.getMethod("itemMethod", null);
        System.out.println(itemMethod.isAnnotationPresent(RepeatSubmitLimit.class));
        if(itemMethod.isAnnotationPresent(RepeatSubmitLimit.class)){
            RepeatSubmitLimit annotation = itemMethod.getAnnotation(RepeatSubmitLimit.class);
            System.out.println(annotation.value()+"-->"+annotation.time());
        }
    }


    @RepeatSubmitLimit("itemMethod")
    @RequestMapping("itemMethod")
    public ResultBean itemMethod(){
        return new ResultBean();
    }
}
