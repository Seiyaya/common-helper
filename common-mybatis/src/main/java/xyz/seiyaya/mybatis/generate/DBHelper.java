package xyz.seiyaya.mybatis.generate;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author seiyaya
 * @date 2019/8/8 19:19
 */
@Slf4j
public class DBHelper {

    public static Connection getConnection(String dbClass,String url,String username,String password){
        Connection connection = null;
        try {
            Class.forName(dbClass);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            log.error("创建连接出现异常",e);
        }

        return connection;
    }
}
