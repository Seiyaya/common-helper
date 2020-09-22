package xyz.seiyaya.proxy;

import xyz.seiyaya.proxy.impl.TestImpl;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 各个代理模式的时间消费查询
 * @author wangjia
 * @version 1.0
 * @date 2020/9/21 14:50
 */
public class ProxyPerfTest {

    public static void main(String[] args) {
        Test nativeTest = new TestImpl();
        Test decorator = new DecoratorTest(nativeTest);
        Test dynamicProxy = DynamicProxyTest.getInstance(nativeTest);
        Test cglibProxy = CglibProxyTest.getInstance(TestImpl.class);

        int preRunCount = 10000;
        runWithoutMonitor(nativeTest, preRunCount);
        runWithoutMonitor(decorator, preRunCount);
        runWithoutMonitor(cglibProxy, preRunCount);
        runWithoutMonitor(dynamicProxy, preRunCount);

        //执行测试；
        Map<String, Test> tests = new LinkedHashMap<String, Test>();
        tests.put("Native   ", nativeTest);
        tests.put("Decorator", decorator);
        tests.put("Dynamic  ", dynamicProxy);
        tests.put("Cglib    ", cglibProxy);
        int repeatCount = 3;
        int runCount = 1000000;
        runTest(repeatCount, runCount, tests);
        runCount = 50000000;
        runTest(repeatCount, runCount, tests);
    }

    private static void runTest(int repeatCount, int runCount, Map<String, Test> tests) {
        System.out.printf("\n==================== run test : [repeatCount=%s] [runCount=%s] [java.version=%s] ====================%n", repeatCount, runCount, System.getProperty("java.version"));
        for (int i = 0; i < repeatCount; i++) {
            System.out.printf("\n--------- test : [%s] ---------%n", (i+1));
            for (String key : tests.keySet()) {
                runWithMonitor(tests.get(key), runCount, key);
            }
        }
    }

    private static void runWithMonitor(Test test, int runCount, String key) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < runCount; i++) {
            test.test(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("["+key + "] Elapsed Time:" + (end-start) + "ms");
    }

    private static void runWithoutMonitor(Test nativeTest, int preRunCount) {
        for (int i = 0; i < preRunCount; i++) {
            nativeTest.test(i);
        }
    }
}
