package xyz.seiyaya.common.web.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/14 10:43
 */
@SpringBootApplication(scanBasePackages = "xyz.seiyaya")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
