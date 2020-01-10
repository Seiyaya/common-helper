# Spring Cloud
spring cloud以前学习过，有时间再将之前的笔记整合过来

## Nacos

### Nacos的配置中心原理
+ 同步本地服务和nacos服务端的模式是本地服务定时去nacos上面去拉取数据
+ nacos测试配置相关代码见`xyz.seiyaya.cloud.test.ConfigServerTester`

+ 同步nacos配置的大致步骤
    1. 创建configService: 通过工厂模式ConfigService实例，最终实例化的是NacosConfigService,包含两个重要的属性HttpAgent和ClientWorker
    2. 调用getConfig()方法获取配置信息,先从本地获取配置信息，没有则从nacos上获取配信息
    3. 添加listener: 监听远程nacos配置项是否有变更，有变更将会同步到本地服务一份新的配置项
    4. CacheData: 缓存具体的配置项，会关联listener在nacos配置项更新的时候回调listener的方法，并更新内部md5的属性，主要用来判断nacos是否有更新过配置
        - 此处保留的不是原始Listener对象，而是包装过的`ManagerListenerWrap`，主要是为了保存上一次拉取nacos配置项的md5的
        - 触发回调的时机就是在对比cacheData和Listener所存的md5不一致的时候就会更新
        

### Nacos的数据持久化
+ 默认情况下Nacos以嵌入式数据库存储，搭建集群目前只有集中式存储一种(mysql存储)
+ 开启持久化只需要在application.properties里面添加对应配置即可(前提条件是已经在数据库执行了对应的sql脚本)
+ 特点: 没有采用zookeeper那样分布式存储配置，而是使用mysql，所以高可用也要依赖mysql的高可用。但是使用起来会相对来说简单
```properties
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://localhost:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=123456
```