package xyz.seiyaya.proxy;

/**
 * 静态代理
 * @author wangjia
 * @version 1.0
 * @date 2020/9/21 14:44
 */
public class DecoratorTest implements Test {

    private Test target;

    public DecoratorTest(Test target){
        this.target = target;
    }

    @Override
    public int test(int i) {
        return target.test(i);
    }
}
