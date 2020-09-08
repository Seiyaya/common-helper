package xyz.seiyaya.dubbo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import xyz.seiyaya.dubbo.api.service.DubboDemoService;

import java.io.IOException;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/11 17:28
 */
@Slf4j
public class MainConsumerStarter {

    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo/all.xml");

        context.start();

        DubboDemoService bean = context.getBean(DubboDemoService.class);
        String s = bean.echoString("msg 123  === ");

        System.out.println(s);

        System.in.read();
    }
}
