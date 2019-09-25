package xyz.seiyaya.jdbc.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.sql.*;
import java.util.Objects;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/25 15:44
 */
@Slf4j
public class DBHelper {

    private static String url = "jdbc:mysql://127.0.0.1:3306/common_tester?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull";
    private static String dbClass = "com.mysql.jdbc.Driver";
    private static String username = "root";
    private static String password = "123456";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(dbClass);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            log.error("创建连接出现异常", e);
        }

        return connection;
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
        if (Objects.nonNull(resultSet)) {
            resultSet.close();
        }

        if (Objects.nonNull(statement)) {
            statement.close();
        }

        if (Objects.nonNull(connection)) {
            connection.close();
        }
    }
}
