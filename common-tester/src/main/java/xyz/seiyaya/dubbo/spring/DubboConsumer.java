package xyz.seiyaya.dubbo.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/18 15:20
 */
public class DubboConsumer {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.start();
    }
}
