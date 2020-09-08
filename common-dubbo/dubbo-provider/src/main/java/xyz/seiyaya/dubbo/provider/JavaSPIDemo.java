package xyz.seiyaya.dubbo.provider;

import xyz.seiyaya.dubbo.api.service.DubboDemoService;

import java.util.ServiceLoader;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/14 10:16
 */
public class JavaSPIDemo {

    /**
     * java的spi演示
     * @param args
     */
    public static void main(String[] args) {
        ServiceLoader<DubboDemoService> load = ServiceLoader.load(DubboDemoService.class);

        for(DubboDemoService dubboDemoService : load){
            String s = dubboDemoService.echoString("123");
            System.out.println(s);
        }
    }
}
