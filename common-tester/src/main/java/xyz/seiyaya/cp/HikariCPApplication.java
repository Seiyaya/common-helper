package xyz.seiyaya.cp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author wangjia
 * @version v1.0
 * @date 2020/11/27 21:14
 */
@SpringBootApplication
@Slf4j
public class HikariCPApplication implements CommandLineRunner {

    @Resource( name = "ordersDataSource")
    private DataSource ordersDataSource;

    @Resource( name = "usersDataSource")
    private DataSource usersDataSource;

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(HikariCPApplication.class, args);

        run.close();
    }

    @Override
    public void run(String... args) throws Exception {
        try(Connection connection = ordersDataSource.getConnection()) {
            log.info("get order connection :{}",connection);
            // get connection :HikariProxyConnection@409007038 wrapping com.mysql.jdbc.JDBC4Connection@6cd15072
        }catch (Exception e){
            log.error("",e);
        }

        try(Connection connection = usersDataSource.getConnection()) {
            log.info("get user connection :{}",connection);
            // get connection :HikariProxyConnection@409007038 wrapping com.mysql.jdbc.JDBC4Connection@6cd15072
        }catch (Exception e){
            log.error("",e);
        }
    }
}
