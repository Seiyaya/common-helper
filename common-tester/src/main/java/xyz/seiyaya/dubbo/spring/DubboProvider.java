package xyz.seiyaya.dubbo.spring;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/18 15:20
 */
public class DubboProvider {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.start();
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "xyz.seiyaya")
    @ComponentScan(value = "xyz.seiyaya")
    @PropertySource("classpath:/dubbo.properties")
    private static class ProviderConfiguration{

    }
}
