package xyz.seiyaya.common.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 16:31
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }
}
