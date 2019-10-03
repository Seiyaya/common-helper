package xyz.seiyaya.common.quartz.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import xyz.seiyaya.common.bean.ConstantBean;
import xyz.seiyaya.common.helper.SpringHelper;
import xyz.seiyaya.common.quartz.bean.QuartzInfo;
import xyz.seiyaya.common.quartz.service.QuartzInfoService;
import xyz.seiyaya.common.quartz.utils.QuartzHelper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import java.util.List;

/**
 * 用来启动的时候加载数据库已经存在的定时任务
 * @author wangjia
 * @version 1.0
 * @date 2019/9/30 15:32
 */
@Slf4j
public class QuartzListener implements ServletContextListener, HttpSessionAttributeListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("all quartz job start ...");

        QuartzInfoService quartzInfoService = SpringHelper.getBean(QuartzInfoService.class);
        QuartzInfo quartzInfo = new QuartzInfo();
        quartzInfo.setState(ConstantBean.NUMBER_ONE);
        List<QuartzInfo> quartzInfoList = quartzInfoService.getList(quartzInfo);

        quartzInfoList.forEach(model->{
            String clName = quartzInfo.getClassName();
            Object cl = null;
            try {
                cl = Class.forName(clName).newInstance();
                QuartzHelper.addJob(quartzInfo);
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
    }
}
