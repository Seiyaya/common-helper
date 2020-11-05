package xyz.seiyaya.mybatis.tester;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.seiyaya.mybatis.bean.UserBean;
import xyz.seiyaya.mybatis.mapper.UserBeanMapper;

import java.util.Arrays;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/31 13:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:mybatis-config.xml")
public class SpringMybatisTester implements ApplicationContextAware {

    private ApplicationContext context;

    private UserBeanMapper userBeanMapper;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Before
    public void init(){
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        Arrays.sort(beanDefinitionNames);
        System.out.println("--BeanNames---");

        Arrays.asList(beanDefinitionNames).subList(0,5).forEach(System.out::println);

        userBeanMapper = context.getBean("UserBeanMapper",UserBeanMapper.class);
        System.out.println("获取到的bean:"+userBeanMapper.getClass());
    }


    @Test
    public void testSelectOne(){
        UserBean user = userBeanMapper.findUser(1);
        System.out.println(user);
    }
}
