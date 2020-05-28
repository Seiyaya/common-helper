package xyz.seiyaya.jvm;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 对象可以在GC自救,这样自救的机会只有一次
 * @author wangjia
 * @version 1.0
 * @date 2020/5/28 10:23
 */
public class FinalizeEscapeGC {

    public static FinalizeEscapeGC finalizeEscapeGC = null;

    public void isAlive(){
        System.out.println("yes i am still alive");
    }

    /**
     * <pre>
     * 执行结果，finalize方法可以自救一次对象
     * finalize method is executed
     * yes i am still alive
     * i am dead
     * -XX:+TraceClassLoading
     * -XX:+TraceClassUnloading
     * -verbose:class
     * </pre>
     * @throws InterruptedException
     */
    @Test
    public void testGC() throws InterruptedException {
        finalizeEscapeGC = new FinalizeEscapeGC();
        finalizeEscapeGC = null;

        System.gc();
        TimeUnit.SECONDS.sleep(1);
        if(finalizeEscapeGC != null){
            finalizeEscapeGC.isAlive();
        }else{
            System.out.println("i am dead");
        }

        finalizeEscapeGC = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);

        if(finalizeEscapeGC != null){
            finalizeEscapeGC.isAlive();
        }else{
            System.out.println("i am dead");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method is executed");
        FinalizeEscapeGC.finalizeEscapeGC = this;
    }
}
