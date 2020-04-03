package xyz.seiyaya.boot.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.boot.event.impl.RainListener;
import xyz.seiyaya.boot.event.impl.SnowListener;
import xyz.seiyaya.boot.event.impl.SnowWeatherEvent;
import xyz.seiyaya.boot.event.impl.WeatherEventMulticaster;

/**
 * 监听器模式
 *  角色: 广播，监听器，事件，触发机制
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ListenerPatternStarter {

    @Autowired
    private WeatherRunListener weatherRunListener;

    @Test
    public void testEvent(){
        weatherRunListener.snow();
        weatherRunListener.rain();
    }

    public static void main(String[] args) {
        WeatherEventMulticaster weatherEventMulticaster = new WeatherEventMulticaster();

        weatherEventMulticaster.addListener(new SnowListener());
        weatherEventMulticaster.addListener(new RainListener());

        weatherEventMulticaster.multicastEvent(new SnowWeatherEvent());
    }
}
