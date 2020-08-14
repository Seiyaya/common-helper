package xyz.seiyaya.dubbo.consumer.config;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/13 16:10
 */
@Configuration
@EnableDubbo(scanBasePackages = "xyz.seiyaya.dubbo")
@ComponentScan(value = "xyz.seiyaya.dubbo")
public class ConsumerConfiguration {


    @Bean
    public ApplicationConfig applicationConfig(){
        ApplicationConfig config = new ApplicationConfig();
        config.setName("dubbo-consumer");
        return config;
    }

    @Bean
    public ConsumerConfig consumerConfig(){
        return new ConsumerConfig();
    }

    @Bean
    public RegistryConfig registryConfig(){
        RegistryConfig config = new RegistryConfig();
        config.setProtocol("zookeeper");
        config.setAddress("localhost");
        config.setPort(2181);
        return config;
    }


}
