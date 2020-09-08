package xyz.seiyaya.dubbo.consumer.config;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import xyz.seiyaya.dubbo.api.service.DubboDemoService;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/13 16:08
 */
@Component
public class DemoService {

    @Reference
    private DubboDemoService dubboDemoService;

    public String echo(String name){
        return dubboDemoService.echoString(name);
    }
}
