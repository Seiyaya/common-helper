package xyz.seiyaya.dubbo.provider;

import org.apache.dubbo.common.extension.ExtensionLoader;
import xyz.seiyaya.dubbo.api.service.DubboDemoService;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/14 10:27
 */
public class DubboSPIDemo {

    public static void main(String[] args) {
        DubboDemoService defaultExtension = ExtensionLoader.getExtensionLoader(DubboDemoService.class).getDefaultExtension();


        String s = defaultExtension.echoString("123");
        System.out.println(s);
    }
}
