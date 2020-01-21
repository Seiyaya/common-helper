package xyz.seiyaya;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import tk.mybatis.spring.annotation.MapperScan;
import xyz.seiyaya.fund.bean.FundAccount;
import xyz.seiyaya.fund.mapper.FundAccountMapper;

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

        PageHelper.startPage(3,10);
        FundAccountMapper bean = run.getBean(FundAccountMapper.class);
        FundAccount fundAccount = bean.selectByPrimaryKey(1);
        log.info("第一次查询:{}",fundAccount);
        fundAccount = bean.selectByPrimaryKey(1);
        log.info("第二次查询:{}",fundAccount);

        run.close();
    }
}
