package xyz.seiyaya.dubbo.consumer.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/13 15:32
 */
public class AnnotationConsumer {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);

        context.start();

        DemoService bean = context.getBean(DemoService.class);
        String s = bean.echo("1235656   ");
        System.out.println(s);
    }
}
