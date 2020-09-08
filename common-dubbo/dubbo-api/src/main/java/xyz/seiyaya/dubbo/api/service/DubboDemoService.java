package xyz.seiyaya.dubbo.api.service;

import org.apache.dubbo.common.extension.SPI;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/11 17:54
 */
@SPI("impl")
public interface DubboDemoService {


    /**
     * 打印字符串
     * @param msg
     */
    String echoString(String msg);
}
