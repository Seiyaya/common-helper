package xyz.seiyaya.dubbo.ext;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Protocol;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/18 10:35
 */
@Slf4j
public class ExtensionLoaderTest {

    @Test
    public void testCreateAdaptiveExtensionClass(){
        Protocol adaptiveExtension = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
        log.info("{}",adaptiveExtension);
    }
}
