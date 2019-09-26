# JDBC
+ mysql版本是5.1.45,8.x新版本的具体驱动类和连接等的实现类都有变化
+ JDBC是一套规范，由厂商实现这些规范这些概念不再赘述
---
+ JDBC接口和Mysql实现之间的关系
    - Connection --> com.mysql.jdbc.JDBC4Connection
    - PreparedStatement --> com.mysql.jdbc.JDBC42PreparedStatement
    - ResultSet --> com.mysql.jdbc.JDBC42ResultSet

## 获取连接
+ 首先回反射对应的驱动类，然后驱动类的静态代码块会向DriverManager种注册自己
    - java.sql.DriverManager.registerDriver(new Driver());
    - 最重driver的引用会被保存到DriverManager中的`registeredDrivers`集合中，方便获取连接的时候使用
+ 数据库连接的本质就是一个TCP长连接
    - DriverManager.getConnection实质上调用的时driver.connect()方法返回连接
    - 在这个过程中会主要包含建立连接以及身份验证 `com.mysql.jdbc.ConnectionImpl.getInstance(host(props), port(props), props, database(props), url);`
    - ` createNewIO(false);`--> `coreConnect()`再该方法创建MysqlIO,该对象进行建立连接(`socketFactory.connect(this.host, this.port, props);`)
    - 连接上之后就回去验证登录名和密码`this.io.doHandshake(this.user, this.password, this.database);`,至此就得到了mysql连接对象
+ 大致的调用流程见`QueryTester`
--- 
```
DriverManager.getConnection(url, username, password);
1.会去调用 Connection con = aDriver.driver.connect(url, info);拿到连接，该连接可以是loadBalance和replication,以下分析普通连接
2.在封装一些属性到properties中后，就可以实例化连接对象com.mysql.jdbc.ConnectionImpl.getInstance(host(props), port(props), props, database(props), url);
3.在实例化过程中主要还是做得配置属性的赋值，赋值完会调用、DatabaseMetaData.getInstance(getMultiHostSafeProxy(), this.database, checkForInfoSchema);实例元数据对象，方便后面建立连接后封装数据
4.最重要的创立连接createNewIO(false);-->coreConnect(mergedProps);-->new MysqlIO(...)-->socketFactory.connect(this.host, this.port, props);这一步只是建立连接，验证密码那些是下一步
5.io.doHandshake(this.user, this.password, this.database); 进行密码验证等信息，最后返回连接
6.AllowMultiQueries 是否允许多个查询
```


## 创建PreparedStatement，并设置参数
```
PreparedStatement.getInstance(getMultiHostSafeProxy(), nativeSql, this.database);创建statement实例
1.要进行预编译必须开启useServerPrepStmts=true，否则PreparedStatement只进行转义，并将拼接好的sql交由jdbc执行
2.如果没有开启useServerPrepStmts的话逻辑就比较简单，只是把参数给保存到数组中
```

## 执行查询
```
preparedStatement.executeQuery();
1.根据上面的sql模板设置参数，结成成完整的sql发给mysql执行
2.Buffer sendPacket = fillSendPacket();进行参数替换问号的操作
3.executeInternal(...)执行sql语句，返回result
```

## 总结
+ jdbc执行一个查询完成，也解释了开始的问题**PreparedStatement处理`?`是在java端还是mysql处理**
    - 结论:在java端处理，交由具体的驱动程序
    - 但是预编译的过程是在mysql进行，会多发送一次RPC来进行prepare
    - 如果是为了提高速度还需要开启`cachePrepStmts=true`,用来缓存sql模板，mysql就会发送一个serverStatementId，下次执行相同的sql模版只需要传递serverStatementId和参数即可