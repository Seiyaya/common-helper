package xyz.seiyaya.jvm;

import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 运行时数据区域
 * @author wangjia
 * @date 2020/5/27 9:58
 */
public class RuntimeRegionTest {

    /**
     * 虚拟机栈测试  StackOverflowError
     * -Xss的大小不是固定的，主要取决于操作系统的分页大小
     * 栈溢出的方式有两种
     *      一种是调用栈的深度超过一定大小
     *      另外一种是变量表超过一定大小
     *  hotspot不支持动态扩展栈的大小
     */
    @Test
    public void testVirtualStack(){
        virtualStackOverflow(0);
    }

    private void virtualStackOverflow(int i) {
        System.out.println(i);
        // 9719
        virtualStackOverflow(i+1);
    }

    /**
     * 堆溢出
     * VM args: -Xms20m -Xmx20m -XX:+heapDumpOnOutOfMemoryError
     */
    @Test
    public void testHeapOOM(){
        //java.lang.OutOfMemoryError: Java heap space
        List<RuntimeRegionTest> list = new ArrayList<>();
        while(true){
            list.add(new RuntimeRegionTest());
        }
    }

    /**
     * 不停new线程导致堆移除
     * VM args: -Xms20m -Xmx20m -Xss5M
     * unable to create native thread
     */
    @Test
    public void testNewThread(){
        while(true){
            new Thread(()->{
                while(true){

                }
            }).start();
        }
    }

    /**
     * <pre>
     * 方法区和运行时常量溢出
     * jdk6及之前可以通过vm参数限制永久代的大小  -XX:PermSize和-XX:MaxPermSize限制永久代大小
     * jdk8之后元空间取代了永久代，存在永久代的字符串常量池被移到堆中
     * jdk6得到的是两个false,jdk7之后得到的是true和false
     * </pre>
     */
    @Test
    public void testMethodRegionOutOfMemory(){
        String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1);

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2);
    }

    /**
     * jdk7执行会出现PermGen space
     */
    @Test
    public void testMethodAreaOOM(){
        while(true){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(RuntimeRegionTest.class);
            enhancer.setUseCache(false);
            enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> methodProxy.invokeSuper(o,objects));
            enhancer.create();
        }
    }
}
