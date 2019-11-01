[toc]

## 常见的类分析
### 1. 重要的类
Configuration: 是一个贯穿整个mybatis声明周期的类，主要是读取配置的xml映射到改类上面，其中包含缓存、mapper等的配置
MappedStatement: 主要是去映射一个sql的xml片段主要作为sql的配置，其中包含入参、出参类型、执行的sql语句等
BoundSql: 主要是用来解析sql和获取运行时参数  
SqlSession: 对外提供操作增删改差的接口  
Executor: 作为sqlSession的调用，也是操作一二级缓存的地方，一级缓存在BaseExecutor，二级缓存在CachingExecutor  
StatementHandler: 主要是作为和jdbc的交互层，比如获取statement和执行sql等   
ParameterHandler: 主要是对参数的处理，比如运行时参数#和$的替换  
ResultHandler: 主要是对结果集的处理  
TypeHandler: 主要是对结果集的处理以及入参的处理     

## JDBC与Mybatis
+ JDBC作为Java平台的数据库访问规范，它仅提供一种访问数据库的能力。这里的每个步骤对于完成一个数据访问请求来说都是必须的
+ Mybatis只是就它做了简化代码，JDBC可以视为一种基础服务，而mybatis是之上的框架

## SQL执行前的准备工作
### 1.解析xml配置信息
+ 通过`XMLConfigBuilder`进行读取xml
+ `parseConfiguration(parser.evalNode("/configuration"));`,然后再解析<configuration/>下的各个子节点
+ 解析` Properties settings = settingsAsPropertiess(root.evalNode("settings"));`setting子节点得到系统设置
```
//解析xml中的setting子节点
Properties settings = settingsAsPropertiess(root.evalNode("settings"));

/**判断setting子节点中的属性是否是都是Configuration，没有则抛出异常
   ReflectorFacory生产Reflector，同时也缓存Reflector
   Reflector主要是用来保存类的哪些可读属性，可写属性，方法引用等，而下面的判断就是判断了那些属性是否可写
   PropertyTokenizer这个主要是用来判断复杂对象里面是否含有指定属性
MetaClass metaConfig = MetaClass.forClass(Configuration.class, localReflectorFactory);
```

## SQL执行

### 1.sql的执行过程之mapper获取
> sqlSession#getMapper --> configuration#getMapper -->  mapperRegistry#getMapper  最后再是代理工厂产生对应的代理mapper  
> MapperRegistry    mapper的注册集合，可以判断某个mapper是否存在等，是在解析xml配置的时候进行添加内容   
MapperProxyFactory  用来生产对应的mapper代理接口  
MapperProxy 代理mapper接口，对mapper接口的方法进行拦截(只拦截非默认方法),执行目标方法的时候会把目标方法封装成MapperMethod   
MapperMethod    缓存需要拦截的方法，主要是去封装了方法的签名信息和对应xml的执行sql信息  
>> SqlCommand   封装的是方法的name和执行sql的类型  
MethodSignature  封装的是方法的签名信息，入参和返回参数等等，作为后面解析sql的一部分    

### 2. 根据配置信息生成SQL，并将运行参数设置到SQL中(查询为例)   
> mapperMethod#execute  --> method#convertArgsToSqlCommandParam
execute()方法执行逻辑: 首先判断SqlCommand的类型，然后执行对应的方法，又会去调用convertArgsToSqlCommandParam()方法来得到执行sql对应的参数  
会将之前解析方法签名得到的param注解参数和执行对应mapper方法传递的参数封装到ParamMap，没有param注解则会将key作为param$i这种  
mapper参数列表只有一个可以直接返回这个对应作为入参，后面可以根据ognl搜索这个对象的属性   
RowBounds  这个参数是mybatis分页的辅助类，只是过滤掉查询出来的数据，不是在数据库层过滤，用途不大   

### 3.一二级缓存实现
> 一级缓存主要是通过BaseExecutor实现，具体的存储是Map结构，key是根据sql和参数以及运行环境封装的`CacheKey`  
二级缓存主要是通过CachingExecutor实现,需要在配置文件里面开启才有效 
--- 
> Cache为顶层接口，具体的实现有永久缓存`PerpetualCache`和LRU的实现`LRUCache`
>> PerpetualCache直接继承HashMap来进行数据存储
>> LRU的最少使用算法是利用了LinkedHashMap来实现，然后再包装Cache，使用到了装饰设计模式
>> BlockingCache具有阻塞特性可重入锁的实现，同一时刻只允许一个线程访问,也是使用了装饰设计模式配一个ConcurrentHashMap的锁集合来实现  
+ cache的key
> 实现是`CacheKey`,重新计算了hashCode的值使得降低在HashMap中的冲突，主要应用在一级缓存和二级缓存
 1. 一级缓存:在`BaseExecutor`上中初始化，会在mybatis执行更新操作或者事务提交的时候进行清空
```
/**
 * CacheKey的hashCode计算
   checkSum原始的hashCode计算和
   count表示的是因子的数量
   multiplier是固定值
   updateList会添加计算的因子
 */
//BaseExecutor 初始化缓存
this.localCache = new PerpetualCache("LocalCache");
//在查询中使用缓存，查询之间创建对象CacheKey
CacheKey key = createCacheKey(ms, parameter, rowBounds, boundSql);
    // 将各个因子作为hashCode计算的因子
    CacheKey cacheKey = new CacheKey();
    cacheKey.update(ms.getId());
    cacheKey.update(Integer.valueOf(rowBounds.getOffset()));
    cacheKey.update(Integer.valueOf(rowBounds.getLimit()));
    cacheKey.update(boundSql.getSql());
//查询不到就查找数据库
list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);

//二级缓存，mybatis会先查找二级缓存，再去查找一级缓存，二级缓存和具体的命名空间绑定
//以及二级缓存还存在事务问题和并发问题，以及缓存不存在是因为它的缓存是和sqlSession一次会话绑定的
//TransactionalCacheManager 维护Cache和TransactionCache的关系

TransactionCache
entriesToAddOnCommit:在事务被提交前，所有的数据库结果缓存在这里，所有的缓存都在这里不再具体的Cache中直到事务提交
entriesMissedInCache：在事务被提交前，CacheKey被缓存在这里，主要和之前的BlockingCache联合使用
```

### 4. 插件机制
> 需要标注注解，并标明需要拦截的具体方法,此处拦截的就是update和query方法   
相关拦截器会被保存在`InterceptorChain`，几乎所有的拦截器、过滤器、插件都是一个逻辑，用链存储具体的功能器，这个也是责任链模式的应用  
插件插件的原理也是对拦截的目标类生成jdk的代理类，然后进行代理
```
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class }) })
//可以拦截的方法
Executor: update, query, flushStatements, commit, rollback,getTransaction, close, isClosed
ParameterHandler: getParameterObject, setParameters
ResultSetHandler: handleResultSets, handleOutputParameters
StatementHandler: prepare, parameterize, batch, update, query
//插件添加的入口
DefaultSqlSessionFactory#openSession  --> Configuration#newExecutor --> InterceptorChain#pluginAll(这里主要是返回拦截对象的代理对象)
//插件执行的入口
Plugin#invoke
```

### 5. 数据库连接的获取和管理

### 6.查询的结果处理，以及延迟加载
sql查询的执行过程  
> SqlSession会调用对用的Executor,默认情况下Executor的实现是CacheExecutor,主要是会多判断一个是否有二级缓存命中的问题,实际上这个只是对SimpleExecutor运用了装饰模式，真正调用的还是SimpleExecutor,  
在这个里面又会去判断是否含有一级缓存，最后采取调用jdbc的statement执行sql  

> MappedStatement: 存储的是一个sql的原始xml片段  
> sqlSource: 是一个接口，主要的实现DynamicSqlSource和RawSqlSource，前者主要是包含#和和where、if标签之类的,首先是将标签里面的内容解析成一个个片段(Node),然后将这些再解析成StaticSqlSource，
    这个过程主要是替换其中的$和#以及内部标签，最终的结果就是含有?的sql语句，每个占位符都会产生对应的ParameterMapping.然后调用StaticSqlSource的getBoundSql  
>> DynamicContext： 是sql语句构建的上下文,每个sql片段解析完成后都会存储这里.  
>> SqlNode：它的各种实现就会解析各种各样的标签  
>> SqlSourceBuilder： 解析含有#{}的内容  
> BoundSql:这是一个很重要的类，存储的是解析玩xml sql片段之后的内容  
> StatementHandler：它的实现则是对应jdbc中statement的实现  

sql更新的执行过程  
> sqlSession#update: sql更新语句执行包含insert、delete  
CachingExecutor#update: 先情况二级缓存，然后调用被装饰的BaseExecutor的方法  
BaseExecutor#update:它又会去先清空一级缓存,然后调用默认实现SimpleExecutor的doUpdate方法  
SimpleExecutor#doUpdate: 产生StatementHandler对象，主要是和jdbc的statement进行交互,所以重点分析的还是StatementHandler#update方法  
StatementHandler#update: 这是一个抽象类，一般我们使用的是`PreparedStatement`,所以实现也选择`PreparedStatementHandler`  
PreparedStatementHandler#update: 这里的重点主要是sql执行完之后，主键的获取，sql执行完之后获取主键KeyGenerator#processAfter  
KeyGenerator#processAfter: 这是一个接口，主要的实现是`Jdbc3KeyGenerator`、`SelectKeyGenerator`、`NoKeyGenerator`