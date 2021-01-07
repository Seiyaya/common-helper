package xyz.seiyaya.base.http;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/7 11:09
 */
public class BaseHttpClientTest {

    protected static final int REQUEST_COUNT = 5;

    protected static final String SEPERATOR = "   ";

    protected static final AtomicInteger NOW_COUNT = new AtomicInteger(0);

    protected static final StringBuilder EVERY_REQ_COST = new StringBuilder(200);


    /**
     * 初始化线程list
     * @param run
     * @return
     */
    protected List<Thread> getRunThreads(Runnable run){
        List<Thread> list = new ArrayList<>(REQUEST_COUNT);

        for(int i=0;i<REQUEST_COUNT;i++){
            list.add(new Thread(run));
        }

        return list;
    }

    protected void startUpAllThreads(List<Thread> list){
        for(Thread t : list){
            t.start();
            // 保证请求按照顺序发出
            try {
                TimeUnit.SECONDS.sleep(3);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    protected synchronized void addCost(long cost){
        EVERY_REQ_COST.append(cost).append("ms").append(SEPERATOR);
    }
}
