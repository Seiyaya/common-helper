package xyz.seiyaya.common.helper;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/10 10:08
 */
@Slf4j
public class TimeHelper {

    public static void sleep(int time){
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            log.error("",e);
        }
    }
}
