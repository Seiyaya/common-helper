package xyz.seiyaya.mybatis.tester;

import lombok.extern.slf4j.Slf4j;
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
 * @author wangjia
 * @version 1.0
 * @date 2019/9/26 15:38
 */
@Slf4j
public class MybatisTester {

    private SqlSessionFactory sqlSessionFactory;


    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    /**
     * 1.读取配置文件
     *  通过 XMLConfigBuilder
     * 2.创建 SqlSessionFactoryBuilder 对象
     *  创建好factory对象后就可以被丢弃
     * 3.通过 SqlSessionFactoryBuilder 对象创建 SqlSessionFactory
     *  factory一旦创建就一直存在
     * 4.通过 SqlSessionFactory 创建 SqlSession
     *  SqlSession 不是线程安全的
     * 5.为 Dao 接口生成代理类
     * 6. 调用接口方法访问数据库
     * @throws IOException
     */
    @Before
    public void init() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        inputStream.close();
    }

    @Test
    public void testMybatis(){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserBeanMapper userBeanMapper = session.getMapper(UserBeanMapper.class);
            UserBean userBean = userBeanMapper.findUser(1);

            log.info("查询到的结果{}",userBean);
        } finally {
            session.commit();
            session.close();
        }
    }
}
