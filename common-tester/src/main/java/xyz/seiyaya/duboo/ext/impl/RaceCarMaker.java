package xyz.seiyaya.duboo.ext.impl;

import com.alibaba.dubbo.common.URL;
import xyz.seiyaya.duboo.ext.CarMaker;
import xyz.seiyaya.duboo.ext.WheelMaker;
import xyz.seiyaya.duboo.ext.bean.Car;

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
        return null;
    }
}
