package xyz.seiyaya.common.cache.task;

import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.common.cache.bean.TimerCacheBean;

import java.util.concurrent.TimeUnit;

/**
 * 清理过期的key定时任务
 * @author wangjia
 * @version 1.0
 * @date 2019/10/8 18:30
 */
@Slf4j
public class CleanExpiredDataTask extends Thread {

    @Override
    public void run() {
        while(true){
            TimerCacheBean.cleanExpiredData();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                log.info("",e);
            }
        }
    }
}
