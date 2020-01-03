package xyz.seiyaya.activiti.bean;

import lombok.Data;

/**
 * 流程审核
 * @author wangjia
 * @version 1.0
 * @date 2020/1/2 17:50
 */
@Data
public class AuditProcess {

    /**
     * 流程流水号
     */
    private Long serialNo;

    /**
     * 是否同意
     */
    private Boolean agree;
    /**
     * 回复内容
     */
    private String content;

    /**
     * 流程实例id
     */
    private String procInsId;

    /**
     * 当前登录用户的id
     */
    private Long userId;

    /**
     * 流程类型
     */
    private String type;
}
