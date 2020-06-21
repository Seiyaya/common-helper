package xyz.seiyaya.boot;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/6/18 16:16
 */
@SpringBootApplication(scanBasePackages = "xyz.seiyaya", exclude = SecurityAutoConfiguration.class)
@MapperScan(value = "xyz.seiyaya", annotationClass = Mapper.class)
public class ProcessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessApplication.class, args);
    }
}
