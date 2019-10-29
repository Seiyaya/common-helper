## JDBC与Mybatis
+ JDBC作为Java平台的数据库访问规范，它仅提供一种访问数据库的能力。这里的每个步骤对于完成一个数据访问请求来说都是必须的
+ Mybatis只是就它做了简化代码，JDBC可以视为一种基础服务，而mybatis是之上的框架

## 大致步骤
### 读取配置文件
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

### 缓存
> Cache为顶层接口，具体的实现有永久缓存`PerpetualCache`和LRU的实现`LRUCache`
>> PerpetualCache直接继承HashMap来进行数据存储
>> LRU的最少使用算法是利用了LinkedHashMap来实现，然后再包装Cache，使用到了装饰设计模式
>> BlockingCache具有阻塞特性可重入锁的实现，同一时刻只允许一个线程访问,也是使用了装饰设计模式配一个ConcurrentHashMap的锁集合来实现
--- 
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


### 插件
> 使用步骤:实现Interceptor接口，标记注解