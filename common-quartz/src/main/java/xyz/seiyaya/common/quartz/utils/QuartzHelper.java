package xyz.seiyaya.common.quartz.utils;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import xyz.seiyaya.common.exception.ParamsException;
import xyz.seiyaya.common.helper.StringHelper;
import xyz.seiyaya.common.quartz.bean.dto.QuartzInfoDto;

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
    public static void addJob(QuartzInfoDto quartzInfoDto) throws Exception {
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
}
