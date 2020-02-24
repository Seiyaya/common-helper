package xyz.seiyaya.boot.event.impl;

import xyz.seiyaya.boot.event.WeatherEvent;

public class SnowWeatherEvent implements WeatherEvent {
    @Override
    public String getWeather() {
        return "snow";
    }
}
