package xyz.seiyaya.duboo.ext;

import com.alibaba.dubbo.common.URL;
import xyz.seiyaya.duboo.ext.bean.Wheel;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 17:37
 */
public interface WheelMaker {

    /**
     * makeWheel
     * @param url
     * @return
     */
    Wheel makeWheel(URL url);
}
