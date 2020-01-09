package xyz.seiyaya.cloud.test;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/9 10:49
 */
@Slf4j
public class ConfigServerTester {

    public static void main(String[] args) throws Exception {
        String serverAddr = "www.seiyaya.xyz:8848";
        String dataId = "nacos-server";

        ConfigService configService = NacosFactory.createConfigService(serverAddr);
        String content = configService.getConfig(dataId, "dev", 5000);

        log.info("获取来自nacos的配置信息:{}",content);

        /*
         * 配置的监听器可以实时监听到nacos端修改了配置项
         * 主要的使用场景: 数据库连接信息的修改，限流规则与降级开关，流量的动态调度
         *
         * 如何做到实时更新nacos服务端的配置信息
         *  通过以定时任务定时同步本地和nacos服务端的配置信息
         */
        configService.addListener(dataId, "dev", new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("配置监听器接受到的配置信息:{}",configInfo);
            }
        });

        int read = System.in.read();
        System.out.println(read);
    }
}
