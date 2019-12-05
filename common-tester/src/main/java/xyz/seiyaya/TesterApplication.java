package xyz.seiyaya;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/5 13:56
 */
@SpringBootApplication(scanBasePackages = "xyz.seiyaya")
@MapperScan(value = "xyz.seiyaya.*.mapper",annotationClass = Mapper.class)
@EnableTransactionManagement
public class TesterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesterApplication.class,args);
    }
}
