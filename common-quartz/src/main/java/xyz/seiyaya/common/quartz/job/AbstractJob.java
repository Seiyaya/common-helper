package xyz.seiyaya.common.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import xyz.seiyaya.common.bean.ConstantBean;
import xyz.seiyaya.common.helper.DateHelper;
import xyz.seiyaya.common.helper.SpringHelper;
import xyz.seiyaya.common.quartz.bean.QuartzInfo;
import xyz.seiyaya.common.quartz.bean.QuartzLog;
import xyz.seiyaya.common.quartz.service.QuartzInfoService;
import xyz.seiyaya.common.quartz.service.QuartzLogService;

import java.util.Date;

/**
 * 实际使用中直接继承该类即可
 * @author wangjia
 * @version 1.0
 * @date 2019/9/30 15:42
 */
@Slf4j
public abstract class AbstractJob implements Job {

    private String jobFuncDesc;

    private String jobCode;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        initMessage();
        QuartzInfoService quartzInfoService = SpringHelper.getBean(QuartzInfoService.class);
        QuartzLogService quartzLogService = SpringHelper.getBean(QuartzLogService.class);

        //获取定时任务信息
        QuartzInfo quartzInfoParams = new QuartzInfo();
        quartzInfoParams.setCode(jobCode);
        quartzInfoParams.setState(ConstantBean.NUMBER_ONE);
        QuartzInfo dbQuartzInfo = quartzInfoService.getByCondition(quartzInfoParams);

        if(null == dbQuartzInfo){
            log.info(" [定时任务][{}] 启动失败:原因未启用! ",jobFuncDesc);
            return;
        }

        //添加执行日志
        QuartzLog quartzLog = new QuartzLog();
        quartzLog.setQuartzId(dbQuartzInfo.getId());
        quartzLog.setStartTime(new Date());

        quartzInfoParams = new QuartzInfo();
        quartzInfoParams.setId(dbQuartzInfo.getId());
        try {
            String remark = doExecute();
            quartzLog.setTime(new Date().getTime() - quartzLog.getStartTime().getTime());
            quartzLog.setResult(ConstantBean.NUMBER_ONE);
            quartzLog.setRemark(remark);

            quartzInfoParams.setSuccess(dbQuartzInfo.getSuccess()+1);

            log.info("[定时任务][{}][{}]",jobFuncDesc, DateHelper.formatNowDate());
        } catch (Exception e) {
            quartzLog.setResult(ConstantBean.NUMBER_ZERO);
            quartzInfoParams.setFail(dbQuartzInfo.getFail()+1);
            log.error("执行定时任务失败", e);
        } finally {
            log.info("保存定时任务日志");
            quartzLogService.insert(quartzLog);

            quartzInfoService.updateById(quartzInfoParams);
        }
    }

    /**
     * 具体的业务逻辑
     * @return
     */
    protected abstract String doExecute();

    /**
     * 初始化提示信息
     * 在这里面调用initMethod("jobCode","jobDescription");
     */
    protected abstract void initMessage();

    /**
     * 具体的定时任务需要添加定时任务描述信息
     * @param jobCode
     * @param jobFuncDesc
     */
    protected void initMethod(String jobCode,String jobFuncDesc){
        this.jobCode = jobCode;
        this.jobFuncDesc = jobFuncDesc;
    }

    /**
     * 方便单元测试的时候测试定时任务
     */
    public void invoke(){
        doExecute();
    }
}
