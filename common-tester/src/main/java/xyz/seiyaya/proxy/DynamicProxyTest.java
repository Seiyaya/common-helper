package xyz.seiyaya.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/21 14:46
 */
public class DynamicProxyTest implements InvocationHandler {

    private Test target;

    public DynamicProxyTest(Test target){
        this.target = target;
    }

    /**
     * 获取代理实例
     * @param target
     * @return
     */
    public static Test getInstance(Test target){
        return (Test) Proxy.newProxyInstance(DynamicProxyTest.class.getClassLoader(),
                new Class<?>[]{Test.class},new DynamicProxyTest(target));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        return method.invoke(target,args);
    }
}
