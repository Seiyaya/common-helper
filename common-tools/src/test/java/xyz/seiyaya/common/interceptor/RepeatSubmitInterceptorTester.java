package xyz.seiyaya.common.interceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.scripting.xmltags.ExpressionEvaluator;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.seiyaya.common.annotation.RepeatSubmitLimit;
import xyz.seiyaya.common.bean.Address;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.bean.User;
import xyz.seiyaya.common.helper.CrawlHelper;

import java.lang.reflect.Method;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/16 9:44
 */
public class RepeatSubmitInterceptorTester {

    @Test
    public void testMethod() throws Exception {
        Method itemMethod = RepeatSubmitInterceptorTester.class.getMethod("itemMethod", User.class);
        Class<?>[] parameterTypes = itemMethod.getParameterTypes();
        if(parameterTypes.length != 0 && parameterTypes[0] != null){
            Class<?> sourcesClazz = parameterTypes[0];
            ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
            System.out.println(sourcesClazz);
        }

        User user = new User();
        user.setUsername("zhangsan");
        user.setAddress(new Address("hubei"));
        Object value = OgnlCache.getValue("address", user);

        System.out.println(value);
        System.out.println(itemMethod.isAnnotationPresent(RepeatSubmitLimit.class));
        if(itemMethod.isAnnotationPresent(RepeatSubmitLimit.class)){
            RepeatSubmitLimit annotation = itemMethod.getAnnotation(RepeatSubmitLimit.class);
            System.out.println(annotation.value()+"-->"+annotation.time());
        }
    }


    @RepeatSubmitLimit("itemMethod")
    @RequestMapping("itemMethod")
    public ResultBean itemMethod(@RequestBody User user){
        return new ResultBean();
    }

    public static void main(String[] args) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","wangjia");

        jsonArray.add(jsonObject);
        try {
            User t = CrawlHelper.getT(User.class, jsonObject);
            System.out.println(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
