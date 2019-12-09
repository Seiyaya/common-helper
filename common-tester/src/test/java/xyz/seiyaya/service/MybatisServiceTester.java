package xyz.seiyaya.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.TesterApplication;
import xyz.seiyaya.mybatis.bean.UserBean;
import xyz.seiyaya.mybatis.service.MybatisService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/5 14:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesterApplication.class)
@EnableAutoConfiguration
@Slf4j
public class MybatisServiceTester {

    @Resource
    private MybatisService mybatisService;

    @Resource
    private SqlSessionFactory sqlSessionFactory;


    @Test
    public void testMybatisCache(){
        mybatisService.findUserBeanById(1);
    }

    @Test
    public void testMybatisRepeatCache(){
        Configuration configuration = sqlSessionFactory.getConfiguration();
        LocalCacheScope localCacheScope = configuration.getLocalCacheScope();
        log.info("一级缓存信息:{} cacheEnable:{}",localCacheScope,configuration.isCacheEnabled());
        mybatisService.findUserBeanRepeatById(1);
    }

    @Test
    public void testSpringTransaction(){
        mybatisService.parentInsert();
    }

    @Test
    public void testStream(){
        mybatisService.findAndUpdateByStream();
    }
}
