package xyz.seiyaya.mybatis.tester;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.junit.Before;
import org.junit.Test;
import xyz.seiyaya.common.cache.helper.DBParam;
import xyz.seiyaya.mybatis.bean.UserBean;
import xyz.seiyaya.mybatis.mapper.UserBeanMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

        /**
         * 先从缓存中拿到对应的MapperMethod，底层调用selectOne方法-->selectList方法取第一个元素，大于1则会抛出异常
         * MapperMethod主要包含两部分数据
         *  1.SqlCommand 具体的sql信息   主要包含type(执行的sql类型)和name(接口报名+方法名)属性
         *  2.MethodSignature 方法签名，主要是方法入参返回值是否集合还是单独的这类boolean类型数据
         * 调用mapperMethod.execute()方法-->
         *      @see DefaultSqlSession#selectOne(java.lang.String, java.lang.Object)-->
         *      根据SqlCommand.name得到MappedStatement,mappedStatement传递给下面的query方法，在内部就会调用mappedStatement.getBoundSql方法，该方法最终又会调用sqlSource.getBoundSql
         *      @see BaseExecutor#query(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler)
         *          @see MappedStatement#getBoundSql(java.lang.Object)
         *          sqlSource.getBoundSql(parameterObject);将动态sql种的#{}转为问号
         *          @see CachingExecutor#createCacheKey(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.mapping.BoundSql)
         *          此处为查找是否含有二级缓存
         *          后面再次调用query方法，会先判断是否有一级缓存，没有则执行query方法
         *          @see BaseExecutor#queryFromDatabase(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.cache.CacheKey, org.apache.ibatis.mapping.BoundSql)
         *
         */
        System.out.println("第一次查询");
        UserBean user = mapper.findUser(1);
        log.info("{}",user);

        //第二次查询使用了缓存
        System.out.println("第二次查询");
        user = mapper.findUser(1);

        log.info("{}",user);

        /**
         * @see BoundSql  主要是存储解析动态标签和#{}占位符之后sql
         * 内部属性
         *      1. String sql;     一个完整的sql，包含占位符等，对应jdbc中需要预处理的sql
         *      2. List<ParameterMapping> parameterMappings;        sql中每个#{}占位符都会被解析成ParameterMapping对象
         *      3. Object parameterObject;                          用户传递的参数的类型
         *      4. Map<String, Object> additionalParameters;        附加集合参数，用于存储databaseId这样的
         *      5. MetaObject metaParameters;                       additionalParameters的元对象信息
         *  @see MappedStatement#getBoundSql(java.lang.Object) 转为调用下面的getBoundSql
         *  @see org.apache.ibatis.mapping.SqlSource#getBoundSql(java.lang.Object)
         *      这个SqlSource有多个实现，DynamicSqlSource主要是来处理含有标签的sql片段，RawSqlSource存储sql的配置信息
         *      主要还是要看DynamicSqlSource#getBoundSql(java.lang.Object)
         *  1. 创建DynamicContext     用来拼接sql的上下文
         *  2. 解析sql片段，将解析结果存储到DynamicContext
         *      需要解析的sql片段都会被映射成SqlNode,实现上有TrimSqlNode、IfSqlNode、MixedSqlNode、WhereSqlNode等
         *  3. 解析sql语句，并构建StaticSqlSource
         *  4. 调用StaticSqlSource的getBoundSql()和BoundSql()
         *  5. 将DynamicContext的contextMap拷贝到BoundSql中
         */
    }


    @Test
    public void printResultMapInfo() throws IOException {
        Configuration configuration = new Configuration();
        String resource = "mapper/UserBeanMapper.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());

        builder.parse();

        ResultMap userBeanMapAll = configuration.getResultMap("userBeanMapAll");

        //用于存储 <id>、<result>、<idArg>、<arg> 节点 column 属性
        log.info("mappedColumn: -->  {}",userBeanMapAll.getMappedColumns());

        System.out.println();
        System.out.println();

        //用于存储 <id> 和 <idArg> 节点对应的 ResultMapping 对象
        userBeanMapAll.getIdResultMappings().forEach(rm -> System.out.println(simplify(rm)));

        System.out.println();
        System.out.println();

        //用于存储 <id> 和 <result> 节点的 property 属性，或 <idArgs> 和 <arg>节点的 name 属性
        userBeanMapAll.getPropertyResultMappings().forEach(rm -> System.out.println(simplify(rm)));

        System.out.println();
        System.out.println();

        //用于存储 <idArgs> 和 <arg> 节点对应的 ResultMapping 对象
        userBeanMapAll.getConstructorResultMappings().forEach(rm -> System.out.println(simplify(rm)));

        System.out.println();
        System.out.println();

        userBeanMapAll.getResultMappings().forEach(rm -> System.out.println(simplify(rm)));
    }

    private String simplify(ResultMapping resultMapping) {
        return String.format("ResultMapping{column='%s', property='%s', flags=%s, ...}",
                resultMapping.getColumn(), resultMapping.getProperty(),
                resultMapping.getFlags());
    }

    /**
     * 一个mapper对应多个xml只需在同一个命名空间即可
     */
    @Test
    public void testSqlExecuteExt(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserBeanMapper mapper = sqlSession.getMapper(UserBeanMapper.class);
        UserBean user = mapper.findExUser(1);
        System.out.println(user);
    }

    @Test
    public void testSqlExecuteWithMapParams(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserBeanMapper mapper = sqlSession.getMapper(UserBeanMapper.class);
        mapper.findUserByCondition(new DBParam().set("id",1).set("name","zhangsan"),"lisi");
    }

    @Test
    public void getUserByLike() throws IOException {
        DBParam dbParam = new DBParam().set("name","%zhang%");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserBeanMapper mapper = sqlSession.getMapper(UserBeanMapper.class);
        UserBean zhangsan = mapper.findUserByCondition(dbParam,null);
        System.out.println(zhangsan);
    }

    @Test
    public void testSqlExecuteWithMapParamsAndForeach(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserBeanMapper mapper = sqlSession.getMapper(UserBeanMapper.class);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        mapper.sqlExecuteWithMapParamsAndForeach(new DBParam().set("list",list));
    }

    @Test
    public void testForeachSqlNode(){
        StaticTextSqlNode staticTextSqlNode = new StaticTextSqlNode("#{item}");
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Configuration configuration = new Configuration();
        ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration,staticTextSqlNode,"list",null,"item","and t.user_id in(",")",",");
        DynamicContext context = new DynamicContext(configuration, null);
        context.bind("list",list);
        forEachSqlNode.apply(context);

        String sql = context.getSql();
        System.out.println(sql);
    }

    /**
     * IfSqlNode中就是利用该方法来进行判断条件
     */
    @Test
    public void testOGNL(){
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        DBParam param = new DBParam();
        param.set("id",1);
        boolean result = evaluator.evaluateBoolean("id != null", param);
        System.out.println(result);
    }

    /**
     * whereSqlNode和SetSqlNode都是基于TrimSqlNode来实现的
     */
    @Test
    public void testWhereSqlNode(){
        String sqlFrame = " and id = #{id}";
        MixedSqlNode mixedSqlNode = new MixedSqlNode(Arrays.asList(new StaticTextSqlNode(sqlFrame)));
        WhereSqlNode whereSqlNode = new WhereSqlNode(new Configuration(), mixedSqlNode);
        DynamicContext dynamicContext = new DynamicContext(new Configuration(), new MapperMethod.ParamMap<>());
        whereSqlNode.apply(dynamicContext);
        System.out.println("解析前:"+sqlFrame);
        System.out.println("解析后:"+dynamicContext.getSql());
    }

    /**
     * 解析mybatis中的占位符，是在上面的解析sql片段结束之后再来解析占位符的问题
     * @see SqlSourceBuilder#parse(java.lang.String, java.lang.Class, java.util.Map)    解析#{}占位符，将解析后的sql添加到StaticSqlSource
     */
    @Test
    public void testParserPlaceHolder(){
        /**
         *  常见占位符处理器，它的handleToken会将#{id} --> ? ,会将#{id}解析成一个map(因为其中还包含type等字段)最终封装到ParameterMapping中
         *  回判断#{}内部的是否已(开头，否则的话会按表达式处理，反之内部就是一个属性名
         *  ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType, additionalParameters);
         *  //创建占位符解析器
         *  GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
         *  //处理占位符，并得到处理之后的sql
         *  String sql = parser.parse(originalSql);
         *  //封装成StaticSqlSource并返回
         *  return new StaticSqlSource(configuration, sql, handler.getParameterMappings());
         */
    }

    /**
     * 处理占位符的代码
     * SqlSourceBuilder.ParameterMappingTokenHandler#buildParameterMapping(java.lang.String)
     * 1. 解析content
     * 2. 解析属性类型
     * 3. 构建ParameterMapping对象
     */
    @Test
    public void testBuildParameterMapping(){
        String sql = "select * from t_test_user where id = #{id,javaType=int,jdbcType=INTEGER}";
        SqlSourceBuilder sqlSourceBuilder = new SqlSourceBuilder(new Configuration());
        SqlSource sqlSource = sqlSourceBuilder.parse(sql, UserBean.class, new HashMap<>());
        BoundSql boundSql = sqlSource.getBoundSql(new UserBean());
        System.out.println("boundSql:"+boundSql.getSql());
        System.out.println("ParameterMappings:"+boundSql.getParameterMappings());
    }

    /**
     * 此处只分析RoutingStatementHandler-->PreparedStatementHandler
     * ms.getStatementType()根据mappedStatement的类型进行选择不同的handler
     * 创建的时机就是之前执行sql判断没有一级缓存之后的queryFromDatabase-->doQuery方法
     * @see org.apache.ibatis.executor.SimpleExecutor#doQuery(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.mapping.BoundSql)
     */
    @Test
    public void testStatementHandler(){
        /**
         * 直接与jdbc交互，在sql执行之前会创建statement接口，填充参数到statement对象中，最重执行sql
         * 并根据之前保存的映射关系进行封装返回查询的数据
         */

        /**
         * @see org.apache.ibatis.executor.SimpleExecutor#prepareStatement(org.apache.ibatis.executor.statement.StatementHandler, org.apache.ibatis.logging.Log)
         *     Connection connection = getConnection(statementLog);
         *     Statement stmt = handler.prepare(connection, transaction.getTimeout());
         *     handler.parameterize(stmt);
         * 1. 创建连接
         * 2. 创建prestatement
         * 3. 设置参数，此处就是封装的jdbc预处理部分的sql
         */

        /**
         * 处理结果集参数映射
         * @see DefaultResultSetHandler#handleResultSets(java.sql.Statement)
         */
    }

    /**
     * 测试执行的mapper是否是同一个实例
     * 按照之前的分析应该不是，是每次都是重新产生一个代理对象
     */
    @Test
    public void testSqlExecuteWithThread() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,10,1, TimeUnit.SECONDS,new LinkedBlockingQueue<>());

        for(int i=0;i<10;i++){
            threadPoolExecutor.execute(()->{
                SqlSession sqlSession = sqlSessionFactory.openSession();
                UserBeanMapper mapper = sqlSession.getMapper(UserBeanMapper.class);
                UserBean user = mapper.findUser(1);
                System.out.println(mapper+"-->"+user);
            });
        }

        Thread.currentThread().join();
    }

    /**
     * 测试org.apache.ibatis.reflection.ParamNameResolver的构造函数 <br/>
     * 参数列表得到的是参数下标和参数名的映射关系 <br/>
     * { 0=0, 1=name }
     */
    @Test
    public void testParamNameResolver() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setUseActualParamName(false);
        Method method = UserBeanMapper.class.getMethod("findUserByCondition", DBParam.class, String.class);
        ParamNameResolver paramNameResolver = new ParamNameResolver(configuration, method);
        Field names = paramNameResolver.getClass().getDeclaredField("names");
        names.setAccessible(true);
        Object o = names.get(paramNameResolver);
        System.out.println(o);
    }
}
