package xyz.seiyaya.stock;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/18 14:37
 */
@SpringBootApplication(scanBasePackages = "xyz.seiyaya")
@Slf4j
@MapperScan(value = "xyz.seiyaya.stock.*.mapper", annotationClass = Mapper.class)
@EnableScheduling
public class StockApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }
}
