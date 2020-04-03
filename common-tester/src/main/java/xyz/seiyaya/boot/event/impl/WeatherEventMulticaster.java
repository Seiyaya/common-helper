package xyz.seiyaya.boot.event.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WeatherEventMulticaster extends AbstractEventMulticaster {


    @Override
    void afterMulticast() {
        log.info("结束广播事件");
    }

    @Override
    void beforeMulticast() {
        log.info("开始广播事件");
    }
}
