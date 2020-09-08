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
        ReferenceConfig<DubboDemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(new ApplicationConfig("dubbo-consumer"));
        referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        referenceConfig.setInterface(DubboDemoService.class);

        DubboDemoService dubboDemoService = referenceConfig.get();
        String s = dubboDemoService.echoString("  01010");
        System.out.println(s);
    }
}
