package xyz.seiyaya.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import xyz.seiyaya.boot.bean.Person;

/** 配置类
 * @author seiyaya
 * @date 2019/11/3 23:43
 */
@Configuration
@ComponentScan(value = "xyz.seiyaya.boot",excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})
})
public class MainConfig {

    /**
     * id即为方法名
     * @return
     */
    @Bean
    public Person person(){
        return new Person("seiyaya",20);
    }
}
