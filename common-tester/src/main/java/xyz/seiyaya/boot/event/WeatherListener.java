package xyz.seiyaya.boot.event;

public interface WeatherListener {
    /**
     * 处理天气事件
     * @param event
     */
    void onWeatherEvent(WeatherEvent event);
}
