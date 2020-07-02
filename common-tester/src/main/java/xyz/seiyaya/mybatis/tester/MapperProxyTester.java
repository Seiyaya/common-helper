package xyz.seiyaya.mybatis.tester;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import xyz.seiyaya.mybatis.bean.UserBean;
import xyz.seiyaya.mybatis.handler.MyMapperProxy;
import xyz.seiyaya.mybatis.mapper.UserBeanMapper;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/2 11:37
 */
public class MapperProxyTester {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void before() throws IOException {
        MybatisTester mybatisTester = new MybatisTester();
        mybatisTester.init();
        sqlSessionFactory = mybatisTester.getSqlSessionFactory();
    }

    @Test
    public void testMapperProxy(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        MyMapperProxy<UserBeanMapper> userBeanMapperMyMapperProxy = new MyMapperProxy<>(UserBeanMapper.class, sqlSession);

        UserBeanMapper userBeanMapper = (UserBeanMapper) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{UserBeanMapper.class}, userBeanMapperMyMapperProxy);
        List<UserBean> list = userBeanMapper.findUserList();
        System.out.println(list);
    }
}
