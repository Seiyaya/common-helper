package xyz.seiyaya.common.quartz.bean;

import lombok.Data;

import java.util.Date;

/**
 * quartz的执行日志
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 14:57
 */
@Data
public class QuartzLog {

    /**
     * 日志id
     */
    private Long id;
    /**
     * 定时任务id
     */
    private Long quartzId;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 执行任务消耗时长
     */
    private long time;
    /**
     * 执行结果
     * 0失败
     * 1正常
     */
    private Integer result;
    /**
     * 备注信息
     */
    private String remark;
}
