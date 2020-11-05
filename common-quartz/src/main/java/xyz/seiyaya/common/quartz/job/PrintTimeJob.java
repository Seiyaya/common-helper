package xyz.seiyaya.common.quartz.job;

import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.common.cache.helper.DateHelper;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/27 10:02
 */
@Slf4j
public class PrintTimeJob extends AbstractJob {

    @Override
    protected String doExecute() {
        log.info("执行打印时间定时任务:{}", DateHelper.formatNowDate());
        return "执行成功";
    }

    @Override
    protected void initMessage() {
        initMethod("printTimeJob","打印当前时间");
    }
}
