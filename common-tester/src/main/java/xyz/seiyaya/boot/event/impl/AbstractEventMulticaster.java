package xyz.seiyaya.boot.event.impl;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.seiyaya.boot.event.EventMulticaster;
import xyz.seiyaya.boot.event.WeatherEvent;
import xyz.seiyaya.boot.event.WeatherListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEventMulticaster implements EventMulticaster {

    @Autowired
    private List<WeatherListener> listenerList;


    @Override
    public void multicastEvent(WeatherEvent weatherEvent) {
        beforeMulticast();
        listenerList.forEach(model->model.onWeatherEvent(weatherEvent));
        afterMulticast();
    }

    /**
     * 广播之后调用
     */
    abstract void afterMulticast();

    /**
     * 广播之前调用
     */
    abstract void beforeMulticast();


    @Override
    public void addListener(WeatherListener weatherListener) {
        if (listenerList == null) {
            listenerList = new ArrayList<>();
        }
        listenerList.add(weatherListener);
    }

    @Override
    public void removeListener(WeatherListener weatherListener) {
        listenerList.remove(weatherListener);
    }
}
