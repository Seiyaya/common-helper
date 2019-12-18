package xyz.seiyaya;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;
import xyz.seiyaya.mybatis.service.MybatisService;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/5 13:56
 */
@SpringBootApplication(scanBasePackages = "xyz.seiyaya")
@MapperScan(value = "xyz.seiyaya.*.mapper",annotationClass = Mapper.class)
@EnableTransactionManagement
@Slf4j
public class TesterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TesterApplication.class, args);
        MybatisService bean = run.getBean(MybatisService.class);
        log.info("{}",bean);

        boolean aopProxy = AopUtils.isAopProxy(bean);
        try {
            Object o = TesterApplication.class.getClassLoader().loadClass("xyz.seiyaya.mybatis.bean.AccountBean").newInstance();
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            log.info("targetClass:{}",targetClass);
            boolean aopProxy1 = AopUtils.isAopProxy(o);

            log.info("aopProxy1 :{}",aopProxy1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("aopProxy :{}",aopProxy);
        run.close();
    }
}
