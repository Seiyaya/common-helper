package xyz.seiyaya.dubbo.provider;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
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
        // 当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig("dubbo-provider");

        // 连接注册中心配置
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");

        ProtocolConfig protocolConfig = new ProtocolConfig("dubbo",20880);

        ServiceConfig<DubboDemoService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setInterface(DubboDemoService.class);
        serviceConfig.setRef(new DemoServiceImpl());

        /**
         * 1. 进一步初始化serviceConfig
         * 2. 校验 ServiceConfig 对象的配置项
         * 3. 使用 ServiceConfig 对象，生成 Dubbo URL 对象数组
         * 4. 使用 Dubbo URL 对象，暴露服务
         */
        serviceConfig.export();


        System.out.println("dubbo provider start");
        System.in.read();
    }
}
