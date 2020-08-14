package xyz.seiyaya.dubbo.provider.config;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/13 16:03
 */
@Configuration
@EnableDubbo(scanBasePackages = "xyz.seiyaya.dubbo")
public class ProviderConfiguration {

    @Bean
    public ProviderConfig providerConfig(){
        return new ProviderConfig();
    }

    @Bean
    public ApplicationConfig applicationConfig(){
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-provider");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig(){
        RegistryConfig config = new RegistryConfig();
        config.setProtocol("zookeeper");
        config.setAddress("localhost");
        config.setPort(2181);
        return config;
    }

    @Bean
    public ProtocolConfig protocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setPort(21990);
        protocolConfig.setName("dubbo");
        return protocolConfig;
    }
}
