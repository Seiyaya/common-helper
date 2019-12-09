package xyz.seiyaya.dubbo.spi.impl;

import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.dubbo.spi.Robot;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 16:40
 */
@Slf4j
public class OptimusPrime implements Robot {

    @Override
    public void sayHello() {
        log.info("optimus prime say");
    }
}
