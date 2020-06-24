package xyz.seiyaya.activiti.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
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
    private String procInsId;

    /**
     * 流程类型:
     **/
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
