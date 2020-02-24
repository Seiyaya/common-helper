package xyz.seiyaya.boot.event;

import xyz.seiyaya.boot.event.impl.RainListener;
import xyz.seiyaya.boot.event.impl.SnowListener;
import xyz.seiyaya.boot.event.impl.SnowWeatherEvent;
import xyz.seiyaya.boot.event.impl.WeatherEventMulticaster;

public class ListenerPatternStarter {

    public static void main(String[] args) {
        WeatherEventMulticaster weatherEventMulticaster = new WeatherEventMulticaster();

        weatherEventMulticaster.addListener(new SnowListener());
        weatherEventMulticaster.addListener(new RainListener());

        weatherEventMulticaster.multicastEvent(new SnowWeatherEvent());
    }
}
