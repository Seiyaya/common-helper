package xyz.seiyaya.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import xyz.seiyaya.common.interceptor.LoginInterceptor;
import xyz.seiyaya.common.interceptor.LoginUserHandlerMethodArgumentResolver;
import xyz.seiyaya.common.interceptor.RepeatSubmitInterceptor;

import java.util.List;

/**
 * 公共的拦截器配置
 * @author wangjia
 * @version 1.0
 * @date 2019/10/16 10:03
 */
@Configuration
@Slf4j
public class CommonInterceptorConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(getRepeatSubmitInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }

    @Bean
    public RepeatSubmitInterceptor getRepeatSubmitInterceptor(){
        return new RepeatSubmitInterceptor();
    }

    @Bean
    public LoginUserHandlerMethodArgumentResolver getLoginUserHandlerMethodArgumentResolver() {
        return new LoginUserHandlerMethodArgumentResolver();
    }


    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(getLoginUserHandlerMethodArgumentResolver());
    }
}
