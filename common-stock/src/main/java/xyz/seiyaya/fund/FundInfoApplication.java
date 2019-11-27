package xyz.seiyaya.fund;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Iterator;

/**
 * @author seiyaya
 * @date 2019/9/29 23:36
 */
@SpringBootApplication(scanBasePackages = "xyz.seiyaya")
@Slf4j
@MapperScan(value = "xyz.seiyaya.*.mapper", annotationClass = Mapper.class)
public class FundInfoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(FundInfoApplication.class, args);
        String name = run.getEnvironment().getProperty("name");
        MutablePropertySources propertySources = run.getEnvironment().getPropertySources();
        Iterator<PropertySource<?>> iterator = propertySources.iterator();
        while(iterator.hasNext()){
            PropertySource<?> next = iterator.next();
            String name1 = next.getName();
            if(next.containsProperty("spring.profiles.active")){
                System.out.println(next.getProperty("spring.profiles.active"));
            }
            System.out.println(name1 + "-->" + next );
        }
        System.out.println(name);

        run.close();
    }
}
