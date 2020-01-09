package xyz.seiyaya.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/9 9:25
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ClientApplication.class, args);
//        run.close();
    }
}
