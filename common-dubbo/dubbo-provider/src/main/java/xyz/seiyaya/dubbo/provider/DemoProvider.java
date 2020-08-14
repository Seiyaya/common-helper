package xyz.seiyaya.dubbo.provider;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import xyz.seiyaya.dubbo.api.service.DubboDemoService;
import xyz.seiyaya.dubbo.provider.service.impl.DemoServiceImpl;

import java.io.IOException;

/**
 * 编码方式启动dubbo
 * @author wangjia
 * @version 1.0
 * @date 2020/8/13 17:06
 */
public class DemoProvider {

    public static void main(String[] args) throws IOException {
        ServiceConfig<DubboDemoService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(new ApplicationConfig("dubbo-provider"));
        serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        serviceConfig.setInterface(DubboDemoService.class);
        serviceConfig.setRef(new DemoServiceImpl());
        serviceConfig.export();


        System.out.println("dubbo provider start");
        System.in.read();
    }
}
