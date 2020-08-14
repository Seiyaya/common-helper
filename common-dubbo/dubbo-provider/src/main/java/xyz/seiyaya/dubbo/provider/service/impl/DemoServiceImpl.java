package xyz.seiyaya.dubbo.provider.service.impl;

import org.apache.dubbo.config.annotation.Service;
import xyz.seiyaya.dubbo.api.service.DubboDemoService;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/12 8:51
 */
@Service
public class DemoServiceImpl implements DubboDemoService {
    @Override
    public String echoString(String msg) {
        System.out.println(msg);
        return String.format("收到:%s", msg);
    }
}
