package xyz.seiyaya.common.quartz.utils;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import xyz.seiyaya.common.cache.helper.StringHelper;
import xyz.seiyaya.common.exception.ParamsException;
import xyz.seiyaya.common.quartz.bean.QuartzInfo;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:26
 */
@Slf4j
public class QuartzHelper {
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";

    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";


    /**
     * 添加一个定时任务,2.x不能直接new JobDetail()和触发器
     * @param quartzInfoDto
     * @throws Exception
     */
    public static void addJob(QuartzInfo quartzInfoDto) throws Exception {
        if(StringHelper.isEmpty(quartzInfoDto.getName()) || StringHelper.isEmpty(quartzInfoDto.getClassName())){
            throw new ParamsException();
        }
        Scheduler scheduler = gSchedulerFactory.getScheduler();
        JobDetail jobDetail= JobBuilder.newJob((Class<? extends Job>) Class.forName(quartzInfoDto.getClassName())).withIdentity(quartzInfoDto.getName(), JOB_GROUP_NAME).build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzInfoDto.getName(), TRIGGER_GROUP_NAME)
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzInfoDto.getCycle())).build();
        scheduler.scheduleJob(jobDetail, trigger);
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

    /**
     * 从quartz容器中移除
     * @param code
     */
    public static void removeJob(String code) throws Exception {
        Scheduler scheduler = gSchedulerFactory.getScheduler();

        TriggerKey triggerKey = new TriggerKey(code, TRIGGER_GROUP_NAME);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);

        //删除定时任务
        JobKey jobKey = new JobKey(code, JOB_GROUP_NAME);
        scheduler.deleteJob(jobKey);
    }

    /**
     * 立即执行定时任务
     * @param code
     */
    public static void startJob(String code) throws Exception{
        Scheduler scheduler = gSchedulerFactory.getScheduler();
        JobKey jobKey = new JobKey(code,JOB_GROUP_NAME);
        scheduler.triggerJob(jobKey);
    }

    /**
     * 启动容器中所有的定时任务
     */
    public static void startJobs() throws SchedulerException {
        Scheduler scheduler = gSchedulerFactory.getScheduler();
        scheduler.start();
    }
}
