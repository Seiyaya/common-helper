package xyz.seiyaya.jdbc;

import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.jdbc.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询tester，作为debug的入口
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/25 15:44
 */
@Slf4j
public class QueryTester {

    public static void main(String[] args) throws SQLException {
        queryUser();
    }

    public static void queryUser() throws SQLException {
        String sql = "select * from t_test_user where id = ?";
        int id = 1;
        Connection connection = DBHelper.getConnection();
        log.info("connection的实现类:{}",connection.getClass());

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        log.info("statement的实现类:{}",preparedStatement.getClass());

        preparedStatement.setInt(1,id);

        ResultSet resultSet = preparedStatement.executeQuery();
        log.info("resultSet的实现类:{}",resultSet.getClass());

        while(resultSet.next()){
            log.info("name:{}",resultSet.getString(2));
        }
    }
}
