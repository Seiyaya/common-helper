package xyz.seiyaya.mybatis.tester;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import xyz.seiyaya.common.helper.DBParam;
import xyz.seiyaya.mybatis.bean.UserBean;
import xyz.seiyaya.mybatis.mapper.UserBeanMapper;

import java.io.IOException;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/2 14:32
 */
public class MybatisCacheTester {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void before() throws IOException {
        MybatisTester mybatisTester = new MybatisTester();
        mybatisTester.init();
        sqlSessionFactory = mybatisTester.getSqlSessionFactory();
    }


    /**
     * 测试一级缓存
     */
    @Test
    public void testLevelOneCache(){
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserBean userBean = sqlSession.selectOne(UserBeanMapper.class.getCanonicalName() + "." + "findUser", new DBParam().set("id", 1));
        System.out.println(userBean.hashCode() + " -->first user :"+ userBean);

        sqlSession.selectOne(UserBeanMapper.class.getCanonicalName() + "." + "findUser", new DBParam().set("id", 1));
        System.out.println(userBean.hashCode() + " -->two user :"+ userBean);
    }


}
