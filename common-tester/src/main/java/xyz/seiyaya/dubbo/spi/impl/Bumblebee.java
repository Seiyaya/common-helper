package xyz.seiyaya.dubbo.spi.impl;

import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.dubbo.spi.Robot;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 16:42
 */
@Slf4j
public class Bumblebee implements Robot {
    @Override
    public void sayHello() {
        log.info("Im bumblebee");
    }
}
