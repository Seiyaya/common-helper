//package xyz.seiyaya.common.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import xyz.seiyaya.common.interceptor.RepeatSubmitInterceptor;
//
///**
// * 公共的拦截器配置
// * @author wangjia
// * @version 1.0
// * @date 2019/10/16 10:03
// */
////@Configuration
//@Slf4j
//public class CommonInterceptorConfig extends WebMvcConfigurationSupport {
//
////    @Value("${repeat.submit.flag}")
//    private String repeatSubmitFlag ;
//
//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
////        log.info("判断是否开启了防止重复提交:{}",repeatSubmitFlag);
////        if("true".equals(repeatSubmitFlag)){
////            registry.addInterceptor(new RepeatSubmitInterceptor()).addPathPatterns("/**");
////        }
//    }
//}
