package xyz.seiyaya.activiti.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.seiyaya.common.annotation.DictFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/11 16:17
 */
@Data
@Table(name = "t_process_base")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActProcess {

    @Id
    private Long id;

    /**
     * 流水号
     **/
    @NotNull(message = "流程水不能为空")
    private String serialNo;

    /**
     * 流程标题
     **/
    private String processTitle;

    /**
     * 发起日期
     **/
    private Date createDate;

    /**
     * 发起人ID
     **/
    private Long createUserId;

    /**
     * 审核状态：0-运行中，1-挂起，2-已完结，3-已驳回，4-审核不通过，5-审核通过，6-已撤销
     **/
    private Byte auditStatus;

    /**
     * 审核时间
     **/
    private Date auditDate;

    /**
     * 流程实例ID
     **/
    private String procInstId;

    /**
     * 流程类型:
     **/
    @DictFormat(type = "PROCESS_TYPE")
    private String type;

    /**
     * 标题中需要加亮的字符串, 用竖线|隔开
     */
    private String highlight;

    /**
     * 流程定义id
     */
    private String procDefId;
}
