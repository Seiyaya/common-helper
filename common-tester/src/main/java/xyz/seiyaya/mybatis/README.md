## JDBC与Mybatis
+ JDBC作为Java平台的数据库访问规范，它仅提供一种访问数据库的能力。这里的每个步骤对于完成一个数据访问请求来说都是必须的
+ Mybatis只是就它做了简化代码，JDBC可以视为一种基础服务，而mybatis是之上的框架

## 大致步骤
1.读取配置文件
    - 通过`XMLConfigBuilder`进行读取xml
    - `parseConfiguration(parser.evalNode("/configuration"));`,然后再解析<configuration/>下的各个子节点