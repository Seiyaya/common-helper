package xyz.seiyaya.dubbo.ext.impl;

import com.alibaba.dubbo.common.URL;
import xyz.seiyaya.dubbo.ext.CarMaker;
import xyz.seiyaya.dubbo.ext.WheelMaker;
import xyz.seiyaya.dubbo.ext.bean.Car;
import xyz.seiyaya.dubbo.ext.bean.RaceCar;
import xyz.seiyaya.dubbo.ext.bean.Wheel;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 17:44
 */
public class RaceCarMaker implements CarMaker {

    private WheelMaker wheelMaker;

    public void setWheelMaker(WheelMaker wheelMaker) {
        this.wheelMaker = wheelMaker;
    }

    @Override
    public Car makeCar(URL url) {
        Wheel wheel = wheelMaker.makeWheel(url);
        return new RaceCar(wheel);
    }
}
