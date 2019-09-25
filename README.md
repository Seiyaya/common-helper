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

## common-activiti
+ 工作流的简单处理

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