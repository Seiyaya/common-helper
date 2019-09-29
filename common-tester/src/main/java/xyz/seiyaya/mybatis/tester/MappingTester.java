package xyz.seiyaya.mybatis.tester;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.junit.Test;
import xyz.seiyaya.mybatis.mapper.UserBeanMapper;

import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * mybatis做映射文件的tester
 * mapping主要包含mapper.xml文件中的节点与java对象之间的映射关系
 * @author wangjia
 * @version 1.0
 * @date 2019/9/27 15:08
 */
public class MappingTester {

    /**
     * 用来测试mybatis的做解析mapper.xml入口
     */
    @Test
    public void testMappingEntrance() throws Exception {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
        XPathParser parser = new XPathParser(resourceAsStream);
        XNode mappersNode = parser.evalNode("/configuration/mappers");
        Method mapperElementMethod = XMLConfigBuilder.class.getDeclaredMethod("mapperElement", XNode.class);
        mapperElementMethod.setAccessible(true);

        //上面读完之后会把流给关掉，本来这两步都是同一步，被我强行拆分成了两步
        resourceAsStream = getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(resourceAsStream);
        mapperElementMethod.invoke(xmlConfigBuilder, mappersNode);
        /**
         * 其中主要做的是
         * 1.解析mapper.xml各个节点
         *      @see XMLMapperBuilder#configurationElement(org.apache.ibatis.parsing.XNode)
         *      1).namespace属性
         *      2).<cache-ref/>     主要用来和别的mapper共用二级缓存
         *      3).<cache/>         主要用来配置二级缓存
         *      4).<resultMap/>
         *      5).<sql/>
         *      6).<select/> <delete/> <update/> <insert/>
         * 2.添加资源路径到现有集合缓存
         * 3.通过命名空间绑定Mapper接口
         */

        /**
         * 此处直接返回的mapper的key视图，也就是key=接口全路径  value时代理工厂
         * knownMappers.put(type, new MapperProxyFactory<T>(type));
         */
        MapperRegistry mapperRegistry = xmlConfigBuilder.getConfiguration().getMapperRegistry();
        mapperRegistry.getMappers().forEach(System.out::println);

        /**
         * 此处返回的就是mapper的代理类
         * 由上面的代理工厂MapperProxyFactory调用newInstance(SqlSession sqlSession)产生对应的MapperProxy
         * 所以代理工厂只有一个，但是代理实例每操作一次数据库就会产生一个mapper的代理对象
         * 在解析xml的过程中会解析一个cache节点和cache-ref节点这个就是来启动该命名空间开启二级缓存
         * cache-ref主要是共享其他命名空间缓存配置
         * 一级缓存时默认开启，也就是session级别的，同一个会话可以缓存
         * 在解析的过程中很多地方都是用了databaseId，主要是用来不同的数据库执行不同的sql
         */
        UserBeanMapper proxyMapper = mapperRegistry.getMapper(UserBeanMapper.class, null);
        System.out.println("userBeanMapper的代理类:"+proxyMapper);
    }
}
