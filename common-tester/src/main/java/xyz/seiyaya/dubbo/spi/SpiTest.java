package xyz.seiyaya.dubbo.spi;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ServiceLoader;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 16:43
 */
@Slf4j
public class SpiTest {

    @Test
    public void sayHello() {
        ServiceLoader<Robot> loader = ServiceLoader.load(Robot.class);
        log.info("call robot say hello");
        loader.forEach(Robot::sayHello);
    }

    @Test
    public void dubboSayHello() {
        ExtensionLoader<Robot> extensionLoader = ExtensionLoader.getExtensionLoader(Robot.class);
        Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
        optimusPrime.sayHello();
        Robot bumblebee = extensionLoader.getExtension("bumblebee");
        bumblebee.sayHello();
    }
}
