package xyz.seiyaya.common.quartz;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 用来测试quartz
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 16:07
 */
@SpringBootApplication(scanBasePackages = "xyz.seiyaya")
@MapperScan(basePackages = {"xyz.seiyaya"},annotationClass = Mapper.class)
public class QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
    }

}
