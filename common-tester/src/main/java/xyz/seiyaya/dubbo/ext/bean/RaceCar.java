package xyz.seiyaya.dubbo.ext.bean;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/18 10:20
 */
public class RaceCar extends Car {

    public RaceCar(Wheel wheel){
        System.out.println("race car add wheel : " + wheel);
    }
}
