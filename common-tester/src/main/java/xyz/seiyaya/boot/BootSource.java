package xyz.seiyaya.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xyz.seiyaya.boot.initializer.SecondInitializer;
import xyz.seiyaya.boot.service.BookService;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@SuppressWarnings("all")
public class BootSource {

    public static void main(String[] args) {
        /**
         * 1. 初始化SpringApplication，然后调用它的run方法
         * @see org.springframework.boot.SpringApplication#run(java.lang.String...)
         * 配置资源加载器
         * 配置primarySources
         * 应用检测
         * 配置系统初始化器
         * 配置应用监听器
         * 配置main方法所在类
         */
        int switchMethod = 1;
        switch (switchMethod){
            case 0:
                staticRun(args);
                break;
            case 1:
                dynamicRun(args);
                break;
            default:
                log.error("error switchMethod");
        }
    }

    private static void dynamicRun(String[] args) {
        SpringApplication springApplication = new SpringApplication(BootSource.class);
        springApplication.addInitializers(new SecondInitializer());
        ConfigurableApplicationContext run = springApplication.run(args);

        String value = run.getEnvironment().getProperty("key3");
        log.info("key3 value:{}",value);

        run.close();
    }

    private static void staticRun(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(BootSource.class, args);

        BookService bean = run.getBean(BookService.class);
        log.info("book service:{} isProxy:{}",bean, AopUtils.isAopProxy(bean));

        run.close();
    }
}
