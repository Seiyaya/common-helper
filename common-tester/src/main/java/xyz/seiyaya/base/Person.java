package xyz.seiyaya.base;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/8 15:45
 */
public class Person {

    @Override
    protected void finalize() throws Throwable {
        System.out.println("person被回收了");
    }
}
