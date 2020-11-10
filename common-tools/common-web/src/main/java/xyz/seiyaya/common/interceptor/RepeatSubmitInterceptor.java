package xyz.seiyaya.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.seiyaya.common.annotation.RepeatSubmitLimit;
import xyz.seiyaya.common.bean.LockBean;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.cache.service.CacheService;
import xyz.seiyaya.common.constant.HttpHeaderConstant;
import xyz.seiyaya.common.constant.ResultConstant;
import xyz.seiyaya.common.helper.JSONHelper;
import xyz.seiyaya.common.helper.SpringHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 防止重复提交，也可以使某个功能串行执行
 * @author wangjia
 * @version 1.0
 * @date 2019/10/16 9:25
 */
@Slf4j
public class RepeatSubmitInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<LockBean> lockKeyThreadLocal = new ThreadLocal<>();

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
            String methodName = method.getDeclaringClass().getName() + method.getName();
            if(repeatSubmitLimit == null){
                log.error("获取repeatSubmitLimit注解信息失败");
                response.setContentType(HttpHeaderConstant.APPLICATION_JSON_UTF8);
                response.getWriter().print(JSONHelper.toJSONString(new ResultBean().setError()));
                return false;
            }
            int time = repeatSubmitLimit.time();

            Object userId = request.getSession().getAttribute("userId");
            String key = userId != null ? methodName + ":" + userId : methodName;
            CacheService bean = SpringHelper.getBean(CacheService.class);
            String value = UUID.randomUUID().toString();
            lockKeyThreadLocal.set(new LockBean(key,value));
            if(!bean.lock(key,value,time)){
                log.error("重复的请求:{}",key);
                response.setContentType(HttpHeaderConstant.APPLICATION_JSON_UTF8);
                response.getWriter().print(JSONHelper.toJSONString(new ResultBean().setCodeAndMsg(ResultConstant.CODE_REPEAT)));
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LockBean lockBean = lockKeyThreadLocal.get();
        if(lockBean != null){
            CacheService cacheService = SpringHelper.getBean(CacheService.class);
            cacheService.unlock(lockBean.getKey(),lockBean.getValue());
        }
        lockKeyThreadLocal.remove();
    }
}
