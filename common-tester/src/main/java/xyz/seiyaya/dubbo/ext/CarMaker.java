package xyz.seiyaya.dubbo.ext;

import com.alibaba.dubbo.common.URL;
import xyz.seiyaya.dubbo.ext.bean.Car;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 17:43
 */
public interface CarMaker {

    /**
     * makeCar
     * @param url
     * @return
     */
    Car makeCar(URL url);
}
