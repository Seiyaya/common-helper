package xyz.seiyaya.dubbo.spi;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 16:39
 */
@SPI
public interface Robot {
    /**
     * say
     */
    void sayHello();
}
