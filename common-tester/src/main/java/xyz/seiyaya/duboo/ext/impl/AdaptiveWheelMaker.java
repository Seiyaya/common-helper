package xyz.seiyaya.duboo.ext.impl;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import xyz.seiyaya.duboo.ext.WheelMaker;
import xyz.seiyaya.duboo.ext.bean.Wheel;

/**
 * 增强类，在代理的方法之前查找具体的实现类
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 17:40
 */
public class AdaptiveWheelMaker implements WheelMaker {
    @Override
    public Wheel makeWheel(URL url) {
        if(url == null){
            throw new IllegalArgumentException("url == null");
        }
        String maker = url.getParameter("Wheel.maker");
        if(maker == null){
            throw new IllegalArgumentException("wheelMakerName == null");
        }

        // 加载具体的实现类
        WheelMaker wheelMaker = ExtensionLoader.getExtensionLoader(WheelMaker.class).getExtension(maker);


        return wheelMaker.makeWheel(url);
    }
}
