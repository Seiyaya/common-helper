package xyz.seiyaya.common.quartz.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;
import xyz.seiyaya.common.bean.ConstantBean;
import xyz.seiyaya.common.helper.SpringHelper;
import xyz.seiyaya.common.quartz.bean.QuartzInfo;
import xyz.seiyaya.common.quartz.service.QuartzInfoService;
import xyz.seiyaya.common.quartz.utils.QuartzHelper;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用来启动的时候加载数据库已经存在的定时任务
 * @author wangjia
 * @version 1.0
 * @date 2019/9/30 15:32
 */
@Slf4j
@Component
public class QuartzListener{


    @PostConstruct
    public void init() {

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                log.error("",e);
            }
            log.info("all quartz job start ...");
            QuartzInfoService quartzInfoService = SpringHelper.getBean(QuartzInfoService.class);
            QuartzInfo quartzInfo = new QuartzInfo();
            quartzInfo.setState(ConstantBean.NUMBER_ONE);
            List<QuartzInfo> quartzInfoList = quartzInfoService.getRealList(quartzInfo);

            quartzInfoList.forEach(model->{
                String clName = model.getClassName();
                Object cl = null;
                try {
                    cl = Class.forName(clName).newInstance();
                    QuartzHelper.addJob(model);
                } catch (Exception e) {
                    log.error("{}不存在或者添加任务异常",clName,e);
                }
            });

            //启动所有刚才被添加的定时任务
            try {
                QuartzHelper.startJobs();
            } catch (SchedulerException e) {
                log.error("启动定时任务异常",e);
            }
            log.info("all quartz job end ...");
        }).start();
    }
}
