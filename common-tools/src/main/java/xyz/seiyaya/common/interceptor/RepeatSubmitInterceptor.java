package xyz.seiyaya.common.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.seiyaya.common.annotation.RepeatSubmitLimit;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.cache.bean.TimerCacheBean;
import xyz.seiyaya.common.constant.HttpHeaderConstant;
import xyz.seiyaya.common.constant.ResultConstant;
import xyz.seiyaya.common.helper.StringHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 防止重复提交，也可以使某个功能串行执行
 * 默认情况下是用内存缓存
 * 从session获取userId，获取不到则是功能的串行化，获取到则是某个人的这个功能串行化
 * @author wangjia
 * @version 1.0
 * @date 2019/10/16 9:25
 */
@Slf4j
public class RepeatSubmitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            Method method = hm.getMethod();
            RepeatSubmitLimit repeatSubmitLimit = null;
            if (!method.isAnnotationPresent(RepeatSubmitLimit.class)) {
                Class<?> clazz = method.getDeclaringClass();
                if (!clazz.isAnnotationPresent(RepeatSubmitLimit.class)) {
                    return true;
                }
            }
            repeatSubmitLimit = method.getAnnotation(RepeatSubmitLimit.class);
            if(repeatSubmitLimit == null){
                log.error("获取repeatSubmitLimit注解信息失败");
                response.setContentType(HttpHeaderConstant.APPLICATION_JSON_UTF8);
                response.getWriter().print(JSON.toJSONString(new ResultBean().setError()));
                return false;
            }
            if(StringHelper.isEmpty(repeatSubmitLimit.value())){
                log.error("{}防止重复提交key标注的有错误",method.getName());
                response.setContentType(HttpHeaderConstant.APPLICATION_JSON_UTF8);
                response.getWriter().print(JSON.toJSONString(new ResultBean().setError()));
                return false;
            }

            Object userId = request.getSession().getAttribute("userId");
            String key = null;
            if (userId != null) {
                key = repeatSubmitLimit.value()+userId;
            }else{
                key = repeatSubmitLimit.value();
            }

            if(StringHelper.isNotEmpty(TimerCacheBean.get(key))){
                response.setContentType(HttpHeaderConstant.APPLICATION_JSON_UTF8);
                response.getWriter().print(JSON.toJSONString(new ResultBean().setCodeAndMsg(ResultConstant.CODE_REPEAT)));
                return false;
            }else{
                request.setAttribute("repeat_key",key);
                TimerCacheBean.set(key, UUID.randomUUID().toString(),repeatSubmitLimit.time());
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //从缓存中移除
        Object repeatKey = request.getAttribute("repeat_key");
        if(repeatKey != null){
            TimerCacheBean.remove(repeatKey.toString());
        }
    }
}
