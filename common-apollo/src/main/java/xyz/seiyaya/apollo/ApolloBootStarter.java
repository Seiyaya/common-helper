package xyz.seiyaya.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/16 16:29
 */
@SpringBootApplication(scanBasePackages = "xyz.seiyaya.apollo")
@EnableApolloConfig
public class ApolloBootStarter {

    public static void main(String[] args) {
        SpringApplication.run(ApolloBootStarter.class,args);
    }
}
