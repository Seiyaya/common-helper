package xyz.seiyaya.boot.event.impl;

import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.boot.event.WeatherEvent;
import xyz.seiyaya.boot.event.WeatherListener;

@Slf4j
public class SnowListener implements WeatherListener {
    @Override
    public void onWeatherEvent(WeatherEvent event) {
        if(event instanceof SnowWeatherEvent){
            log.info("接收到snow事件:{}",event.getWeather());
        }
    }
}
