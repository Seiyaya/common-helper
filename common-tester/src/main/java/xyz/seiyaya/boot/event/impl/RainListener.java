package xyz.seiyaya.boot.event.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.seiyaya.boot.event.WeatherEvent;
import xyz.seiyaya.boot.event.WeatherListener;

@Slf4j
@Component
public class RainListener implements WeatherListener {
    @Override
    public void onWeatherEvent(WeatherEvent event) {
        if(event instanceof RainWeatherEvent){
            log.info("接收到rain事件:{}",event.getWeather());
        }
    }
}
