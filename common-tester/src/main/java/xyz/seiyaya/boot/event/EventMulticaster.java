package xyz.seiyaya.boot.event;

/**
 * 广播事件
 */
public interface EventMulticaster {

    /**
     * 广播事件
     * @param weatherEvent
     */
    void multicastEvent(WeatherEvent weatherEvent);

    /**
     * 添加监听器
     * @param weatherListener
     */
    void addListener(WeatherListener weatherListener);

    /**
     * 移除监听器
     * @param weatherListener
     */
    void removeListener(WeatherListener weatherListener);
}
