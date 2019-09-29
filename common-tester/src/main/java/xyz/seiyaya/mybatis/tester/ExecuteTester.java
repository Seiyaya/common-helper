package xyz.seiyaya.mybatis.tester;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import xyz.seiyaya.mybatis.bean.UserBean;
import xyz.seiyaya.mybatis.mapper.UserBeanMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * 执行sql的过程
 * @author wangjia
 * @version 1.0
 * @date 2019/9/29 9:40
 */
@Slf4j
public class ExecuteTester {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        inputStream.close();
    }

    @Test
    public void testSqlExecute(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        /**
         * 首先是创建mapper接口的代理类，然后执行方法的时候会调用
         *      @see MapperProxy#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
         *      1.首先判断是否是object的方法，是的话直接执行
         *      2.不是的话从缓存`cacheMethod`取出对应的方法，没有就创建然后保存到cacheMethod中，key是Method，value时mapperMethod
         */
        UserBeanMapper mapper = sqlSession.getMapper(UserBeanMapper.class);

        UserBean user = mapper.findUser(1);

        log.info("{}",user);
    }
}
