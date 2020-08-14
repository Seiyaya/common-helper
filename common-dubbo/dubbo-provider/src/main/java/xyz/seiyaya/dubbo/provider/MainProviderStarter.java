package xyz.seiyaya.dubbo.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/11 16:01
 */
@Slf4j
public class MainProviderStarter {

    public static void main(String[] args) throws IOException {
        /**
         * 两种启动方式
         * 1. 直接初始化spring context来
         * 2. 通过 com.alibaba.dubbo.container.Main 利用扩展点来加载Spring容器，然后激活Spring框架配置， 具体实现参考 SpringContainer
         */
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo/all.xml");

        context.start();

        log.info("dubbo provider start");

        System.in.read();
    }
}
