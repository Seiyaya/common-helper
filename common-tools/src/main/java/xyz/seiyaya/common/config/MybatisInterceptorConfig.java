package xyz.seiyaya.common.config;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.seiyaya.common.interceptor.SqlLogInterceptor;

import java.util.Properties;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/27 10:57
 */
@Configuration
public class MybatisInterceptorConfig {

    @Bean
    public Interceptor sqlLogInterceptor(){
        return new SqlLogInterceptor();
    }

    @Bean
    public Interceptor pageHelperInterceptor(){
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        // 属性reasonable 参数合理化，也就是说如果查询页*页大小 >  总记录数，会查询最后一页的数据
        properties.setProperty("dialect", "mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
