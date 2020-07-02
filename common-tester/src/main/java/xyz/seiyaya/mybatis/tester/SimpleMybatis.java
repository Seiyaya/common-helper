package xyz.seiyaya.mybatis.tester;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.LoggingCache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import xyz.seiyaya.common.interceptor.SqlLogInterceptor;
import xyz.seiyaya.mybatis.bean.UserBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/2 16:08
 */
public class SimpleMybatis {

    public static void main(String[] args) throws SQLException {
        LogFactory.useSlf4jLogging();
        Configuration configuration = new Configuration();

        // 添加配置项(对应config)
        configuration.setCacheEnabled(true);
        configuration.setLazyLoadingEnabled(false);
        configuration.setAggressiveLazyLoading(true);

        // 添加拦截器
        SqlLogInterceptor sqlLogInterceptor = new SqlLogInterceptor();
        configuration.addInterceptor(sqlLogInterceptor);

        // 创建数据源和jdbc事务
        UnpooledDataSource dataSource = new UnpooledDataSource();
        dataSource.setDriver("");
        dataSource.setUrl("");
        dataSource.setUsername("");
        dataSource.setPassword("");

        Transaction transaction = new JdbcTransaction(dataSource, null, false);

        // 创建执行器
        Executor executor = configuration.newExecutor(transaction);

        // 创建sqlSource对象
        StaticSqlSource sqlSource = new StaticSqlSource(configuration, "select * from t_user where id = ?");

        // 添加sql的参数映射
        List<ParameterMapping> paramList = new ArrayList<>();
        paramList.add(new ParameterMapping.Builder(configuration,"id",configuration.getTypeHandlerRegistry().getTypeHandler(Long.class)).build());
        ParameterMap parameterMap = new ParameterMap.Builder(configuration, "defaultParameterMap", UserBean.class, paramList).build();

        // 处理结果集映射
        List<ResultMapping> resultMappingList = new ArrayList<>();
        resultMappingList.add(new ResultMapping.Builder(configuration,"id","id",Integer.class).build());
        resultMappingList.add(new ResultMapping.Builder(configuration,"name","name",Integer.class).build());
        ResultMap resultMap = new ResultMap.Builder(configuration, "defaultResultMap", UserBean.class, resultMappingList).build();

        // 创建缓存对象
        Cache cache = new SynchronizedCache(new SerializedCache(new LoggingCache(new LruCache(new PerpetualCache("userBeanCache")))));

        // 创建mappedStatement
        MappedStatement ms = new MappedStatement.Builder(configuration, "xyz.seiyaya.mybatis.mapper.UserBeanMapper.findUser", sqlSource, SqlCommandType.SELECT)
                .parameterMap(parameterMap)
                .resultMaps(Collections.singletonList(resultMap))
                .cache(cache).build();

        configuration.addMappedStatement(ms);

        // 创建session
        DefaultSqlSession sqlSession = new DefaultSqlSession(configuration, executor, false);
        UserBean userBean = sqlSession.selectOne("xyz.seiyaya.mybatis.mapper.UserBeanMapper.findUser", 1);

        System.out.println(userBean);
    }
}
