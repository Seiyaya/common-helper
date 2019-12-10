# 常用的代码片段


## common-mybatis
+ 打印sql日志的拦截器
    - 入口:SqlLogInterceptor(已迁移至tools模块下面)
    
+ 根据数据库表配置自动生成Bean、接口mapper、service、mapper.xml
    - 入口:~~MybatisStarter~~(已废弃，先全部使用tkMybatis更加简洁)

## common-quartz
+ 数据库配置定时任务，通过后台管理系统直接管理定时任务

## common-manage
+ 权限管理系统
    - 包含权限、角色、用户、菜单后台相关代码

## common-tools
### 常用注解
+ `RepeatSubmitLimit`: 防止重复提交的注解: 可以通过expression来配置需要作为锁定的key，会在第一个参数中通过ognl查找对应的key的组成部分
+ `CrawlAttribute`注解: 标注在bean上面主要是快速映射字段，想把一个对象的属性复制到另外一个属性上面
+ `AuthorityTableData`注解: 后端控制权限的列表

### 常用工具类
+ `MybatisSqlHelper`: 主要用来快速得到xml片段参数化后的sql,方便更快的调试。
+ `RepeatSubmitInterceptor`: 防止重复提交的拦截器，目前只实现了针对userId的，后面可以直接根据传递的参数通过ognl表达式得到针对一个接口的防止重复提交

## common-deploy
### 目标
> 制作简易版“jenkins”,通过shell脚本来达到jenkins相关功能  

### 适用场景
>   1. 个人站点不想引入jenkins这种重量级软件  
>   2. spring boot项目避免上传整个jar包  

### 原理
> 在服务端维护一份git代码的副本，通过一系列的脚本来进行打包、重启等功能  
> 后面可以通过写文件的方式来记录历史jar包和历史git提交等信息

## common-tester
+ 主要是做一些源码的测试类，或者一些不明白、不太清楚的地方的Tester

### xyz.seiyaya.jdbc
+ 从最开始学习JDBC就知道编码的几步，但是从来没有看过具体过程。
+ 也在面试过程被问到预编译PreparedStatement处理`?`是在java端还是mysql处理的。特此决定好好学习一下具体实现
+ 常见的jdbc开发步骤(详情见**xyz.seiyaya.jdbc包下的README.md**)
    1. 加载数据库驱动
    2. 通过数据库驱动管理器获取连接
    3. 创建PreparedStatement,并设置对应sql的参数
    4. 执行查询

### xyz.seiyaya.mybatis
+ 也是在面试过程中被问及一个mapper接口对应多个xml文件要怎么处理，以前只知道一个mapper对应一个xml的处理方式
+ 另外一个原因也是因为毕业后的第一家公司自己基于mybatis做的封装，一直没有去完整的去看它如何定制化的更符合公司内部框架

### xyz.seiyaya.mq
+ 用来测试消息队列RabbitMQ
    - 生产者: 使用spring的`AmqpTemplate`进行发送消息
    - 消费者: 使用`@RabbitListener`来声明处理器类以及需要监听的消息名称，再使用`@RabbitHandler`来声明处理消息的方法
+ 需要注意的地方
    - 消费消息是异步的，如果需要等待其他处理做完后再做处理可以拒绝消息重新加入队列
    - 默认情况下处理的方法抛出异常会一直重发消息，需要做相应的处理
    - 一个消息无法发送给监听多个同名的消息处理器(需要使用交换机来进行相应处理)

### xyz.seiyaya.dubbo
+ dubbo相关测试

### xyz.seiyaya.collection
+ 集合框架相关测试,主要针对jdk已实现的比如List、Map、JUC包下的集合等

### xyz.seiyaya.thread
+ 主要是线程池JUC包下的并发工具类

### xyz.seiyaya.io
+ 主要是IO的各种实现,BIO、NIO、AIO以及它们的应用

### xyz.seiyaya.boot
+ 主要是boot的启动流程以及各个时间点的回调等等