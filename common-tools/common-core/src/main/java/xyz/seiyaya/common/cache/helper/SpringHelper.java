package xyz.seiyaya.common.cache.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/9/30 15:34
 */
@Component
public class SpringHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取bean
     * @param beanName
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName,Class<T> clazz){
        return context.getBean(beanName,clazz);
    }

    /**
     * 获取bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return context.getBean(clazz);
    }

    /**
     * 获取spring上下文
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        return context;
    }
}
