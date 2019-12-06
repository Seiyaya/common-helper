package xyz.seiyaya.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import sun.reflect.Reflection;
import xyz.seiyaya.jdbc.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询tester，作为debug的入口
 * @author wangjia
 * @version 1.0
 * @date 2019/9/25 15:44
 */
@Slf4j
public class QueryTester {

    public static void main(String[] args) throws SQLException {
        queryUser();
    }

    @SuppressWarnings("all")
    private static void queryUser() throws SQLException {
        String sql = "select * from t_test_user where id = ?";
        int id = 1;
        /**
         * 1.Class.forName(dbClass);
         * 反射加载驱动，首先会调用驱动的静态方法，该静态方法就会向DriverManager种注册自己
         * 该驱动的父类NonRegisteringDriver又会反射AbandonedConnectionCleanupThread，该线程主要用来关闭废弃的连接
         * 会将驱动的引用存储到registeredDrivers中
         *
         * 2. DriverManager.getConnection(url, username, password);
         * 会去调用 Connection con = aDriver.driver.connect(url, info);拿到连接，该连接可以是loadBalance和replication,以下分析普通连接
         * 在封装一些属性到properties中后，就可以实例化连接对象com.mysql.jdbc.ConnectionImpl.getInstance(host(props), port(props), props, database(props), url);
         * 在实例化过程中主要还是做得配置属性的赋值，赋值完会调用、DatabaseMetaData.getInstance(getMultiHostSafeProxy(), this.database, checkForInfoSchema);实例元数据对象，方便后面建立连接后封装数据
         * 最重要的创立连接createNewIO(false);-->coreConnect(mergedProps);-->new MysqlIO(...)-->socketFactory.connect(this.host, this.port, props);这一步只是建立连接，验证密码那些是下一步
         * io.doHandshake(this.user, this.password, this.database); 进行密码验证等信息，最后返回连接
         * AllowMultiQueries 是否允许多个查询
         *
         * 3.connection.prepareStatement(sql);
         * PreparedStatement.getInstance(getMultiHostSafeProxy(), nativeSql, this.database);创建statement实例
         * 要进行预编译必须开启useServerPrepStmts=true，否则PreparedStatement只进行转义，并将拼接好的sql交由jdbc执行
         * 如果没有开启useServerPrepStmts的话逻辑就比较简单，只是把参数给保存到数组中
         *
         * 4.preparedStatement.executeQuery();
         * 根据上面的sql模板设置参数，结成成完整的sql发给mysql执行
         * Buffer sendPacket = fillSendPacket();进行参数替换问号的操作
         * executeInternal(...)执行sql语句，返回result
         */
        Connection connection = DBHelper.getConnection();
        log.info("connection的实现类:{}",connection.getClass());

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        log.info("statement的实现类:{}",preparedStatement.getClass());

//        preparedStatement.setInt(1,id);
        preparedStatement.setString(1,"1' or 1=1;delete from t_test_user");

        ResultSet resultSet = preparedStatement.executeQuery();
        log.info("resultSet的实现类:{}",resultSet.getClass());

        while(resultSet.next()){
            log.info("name:{}",resultSet.getString(2));
        }
    }

    @Test
    public void testCallerClass(){
        log.info("{}", Reflection.getCallerClass());
    }
}
