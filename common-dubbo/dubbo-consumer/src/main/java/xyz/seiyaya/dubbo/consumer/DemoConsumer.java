package xyz.seiyaya.dubbo.consumer;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import xyz.seiyaya.dubbo.api.service.DubboDemoService;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/13 17:09
 */
public class DemoConsumer {

    public static void main(String[] args) {
        // 当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-consumer");

        // 连接注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");

        // 引用远程服务
        ReferenceConfig<DubboDemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(DubboDemoService.class);

        DubboDemoService dubboDemoService = referenceConfig.get();
        String s = dubboDemoService.echoString("  01010");
        System.out.println(s);
    }
}
