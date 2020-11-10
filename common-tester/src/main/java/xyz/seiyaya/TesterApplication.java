package xyz.seiyaya;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.MDC;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;
import xyz.seiyaya.common.helper.SpringHelper;
import xyz.seiyaya.mybatis.service.MybatisService;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/5 13:56
 */
@SpringBootApplication(scanBasePackages = "xyz.seiyaya")
@MapperScan(value = {"xyz.seiyaya.*.mapper","xyz.seiyaya.*.dao"}, annotationClass = Mapper.class)
@EnableTransactionManagement
@Slf4j
@SuppressWarnings("all")
public class TesterApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(TesterApplication.class, args);
        MybatisService bean = run.getBean(MybatisService.class);
        log.info("{}", bean);

        if(false){
            testGenerateClass(run, bean);
        }

        MDC.clear();
        MDC.put("requestId", UUID.randomUUID().toString());

        log.info("print:{}",System.currentTimeMillis());
//        run.close();
    }

    private static void testGenerateClass(ConfigurableApplicationContext run, MybatisService bean) throws ClassNotFoundException, IOException {
        boolean aopProxy = AopUtils.isAopProxy(bean);
        try {
            Object o = TesterApplication.class.getClassLoader().loadClass("xyz.seiyaya.mybatis.bean.AccountBean").newInstance();
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            log.info("targetClass:{}", targetClass);
            boolean aopProxy1 = AopUtils.isAopProxy(o);

            log.info("aopProxy1 :{}", aopProxy1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("aopProxy :{}", aopProxy);

        for (String beanDefinitionName : run.getBeanDefinitionNames()) {
            log.info("{}",beanDefinitionName);
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, "D:\\wangjia\\MyCode\\common-helper\\common-tester\\src\\main\\java\\xyz\\AutoCompilerServiceImpl.java");
        log.info("{}", compilationResult);

        try {
            Object autoCompilerService = run.getBean("AutoCompilerServiceImpl");
            log.info("刷新前:{}",autoCompilerService);
        }catch (Exception e){
            log.error("找不到对应的bean",e);
        }

        try {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition("xyz.AutoCompilerServiceImpl");
            beanDefinitionBuilder.addDependsOn("userBeanMapper");
            ((DefaultListableBeanFactory)run.getBeanFactory()).registerBeanDefinition("AutoCompilerServiceImpl",beanDefinitionBuilder.getRawBeanDefinition());
            Object autoCompilerService = run.getBean("AutoCompilerServiceImpl");
            log.info("刷新后:{} --> {}",autoCompilerService, SpringHelper.getBean(MybatisService.class));

            autoCompilerService.getClass().getMethod("testInsert").invoke(autoCompilerService);
        }catch (Exception e){
            log.error("",e);
        }

        SpringHelper.getBean(MybatisService.class).printBean();

        log.info("重新load后");
        compiler = ToolProvider.getSystemJavaCompiler();
        compilationResult = compiler.run(null, null, null, "D:\\PrintBean.java");
        FileUtils.copyFile(new File("D:\\PrintBean.class"),new File("D:\\wangjia\\MyCode\\common-helper\\common-tester\\target\\classes\\xyz\\seiyaya\\mybatis\\bean\\PrintBean.class"));
        log.info("{}", compilationResult);
        TesterApplication.class.getClassLoader().loadClass("xyz.seiyaya.mybatis.bean.PrintBean");
        SpringHelper.getBean(MybatisService.class).printBean();
    }
}
