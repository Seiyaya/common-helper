# 常用的代码片段


## common-mybatis
+ 打印sql日志的拦截器
    - 已实现，入口:SqlLogInterceptor
    
+ 根据数据库表配置自动生成Bean、接口mapper、service、mapper.xml
    - 实现中，入口:MybatisStarter

## common-quartz
+ 数据库配置定时任务，通过后台管理系统直接管理定时任务

## common-manage
+ 权限管理系统
    - 包含权限、角色、用户、菜单后台相关代码

## common-tools
+ 常用的工具类以及代码片段
> RepeatSubmitLimit防止重复提交的注解
>> 可以通过expression来配置需要作为锁定的key，会在第一个参数中通过ognl查找对应的key的组成部分

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