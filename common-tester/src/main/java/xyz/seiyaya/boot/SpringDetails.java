package xyz.seiyaya.boot;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xyz.seiyaya.boot.config.MainConfig;

/**
 * spring注解和内部原理相关
 * @author seiyaya
 * @date 2019/11/3 23:41
 */
public class SpringDetails {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        Object person = context.getBean("person");
        System.out.println(person);

        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor
         * org.springframework.context.annotation.internalCommonAnnotationProcessor
         * org.springframework.context.event.internalEventListenerProcessor
         * org.springframework.context.event.internalEventListenerFactory
         * mainConfig
         * bookController
         * bookDao
         * bookService
         * person
         */
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
