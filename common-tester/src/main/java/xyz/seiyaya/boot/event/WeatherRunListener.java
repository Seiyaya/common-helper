package xyz.seiyaya.boot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.seiyaya.boot.event.impl.RainWeatherEvent;
import xyz.seiyaya.boot.event.impl.SnowWeatherEvent;
import xyz.seiyaya.boot.event.impl.WeatherEventMulticaster;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/4/3 14:09
 */
@Component
public class WeatherRunListener {

    @Autowired
    private WeatherEventMulticaster weatherEventMulticaster;


    public void snow(){
        weatherEventMulticaster.multicastEvent(new SnowWeatherEvent());
    }

    public void rain(){
        weatherEventMulticaster.multicastEvent(new RainWeatherEvent());
    }
}
