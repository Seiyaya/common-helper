package xyz.seiyaya.dubbo.provider.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/13 16:00
 */
public class AnnotationProvider {

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProviderConfiguration.class);

        context.start();

        System.in.read();
    }
}
