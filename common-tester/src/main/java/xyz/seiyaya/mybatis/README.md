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