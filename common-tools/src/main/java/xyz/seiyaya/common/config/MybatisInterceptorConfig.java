package xyz.seiyaya.common.config;

import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.seiyaya.mybatis.interceptor.SqlLogInterceptor;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/27 10:57
 */
@Configuration
public class MybatisInterceptorConfig {

    @Bean
    public Interceptor getInterceptor(){
        return new SqlLogInterceptor();
    }

}
