package xyz.seiyaya.fund;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author seiyaya
 * @date 2019/9/29 23:36
 */
@SpringBootApplication(scanBasePackages = "xyz.seiyaya")
@Slf4j
@MapperScan(value = "xyz.seiyaya.*.mapper", annotationClass = Mapper.class)
public class FundInfoApplication {
    public static void main(String[] args) {
        SpringApplication.run(FundInfoApplication.class,args);
    }
}
