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

## 创建PreparedStatement，并设置参数
+ 