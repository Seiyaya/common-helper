package xyz.seiyaya.common.quartz.bean;

import lombok.Data;

import java.util.Date;

/**
 * 具体的某个定时任务
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 14:56
 */
@Data
public class QuartzInfo {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 定时任务的名称
     */
    private String name;
    /**
     * 定时任务的英文名称
     */
    private String code;
    /**
     * cron表达式
     */
    private String cycle;
    /**
     * 定时任务所在的类
     */
    private String className;
    /**
     * 成功次数
     */
    private Integer success;
    /**
     * 失败次数
     */
    private Integer fail;
    /**
     * 状态
     * state=0禁用
     * state=1启用
     */
    private Integer state;
    /**
     * 定时任务创建时间
     */
    private Date createDate;
}
