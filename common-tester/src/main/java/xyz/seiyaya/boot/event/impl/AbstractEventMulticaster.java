package xyz.seiyaya.boot.event.impl;

import xyz.seiyaya.boot.event.EventMulticaster;
import xyz.seiyaya.boot.event.WeatherEvent;
import xyz.seiyaya.boot.event.WeatherListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEventMulticaster implements EventMulticaster {

    private List<WeatherListener> listenerList = new ArrayList<>();


    @Override
    public void multicastEvent(WeatherEvent weatherEvent) {
        beforeMulticast();
        listenerList.forEach(model->model.onWeatherEvent(weatherEvent));
        afterMulticast();
    }

    abstract void afterMulticast();

    abstract void beforeMulticast();


    @Override
    public void addListener(WeatherListener weatherListener) {
        listenerList.add(weatherListener);
    }

    @Override
    public void removeListener(WeatherListener weatherListener) {
        listenerList.remove(weatherListener);
    }
}
