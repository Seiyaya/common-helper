package xyz.seiyaya.dubbo.provider.parser;

import org.apache.dubbo.config.spring.schema.DubboNamespaceHandler;
import org.junit.Test;

/**
 * 用来测试dubbo如何解析配置
 * @author wangjia
 * @version 1.0
 * @date 2020/8/14 15:48
 */
public class ParserTest {

    @Test
    public void testDubboNamespaceHandler(){
        // duboo属性注册解析器
        DubboNamespaceHandler dubboNamespaceHandler = new DubboNamespaceHandler();
        dubboNamespaceHandler.init();
    }
}
