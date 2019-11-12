package xyz.seiyaya.activiti.bean;

import lombok.Data;
import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.task.Task;
import org.junit.Test;
import xyz.seiyaya.activiti.mapper.ActProcessMapper;

import java.util.Date;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/11 16:17
 */
@Data
public class ActProcess {
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
    private String createUserId;

    /**
     * 发起人
     **/
    private String createUser;

    /**
     * 审核状态：0-运行中，1-挂起，2-已完结，3-已驳回，4-审核不通过，5-审核通过，6-已撤销
     **/
    private Integer auditStatus;

    /**
     * 审核状态字符串
     */
    private String auditStatusStr;

    /**
     * 审核状态颜色
     */
    private String statusColor;

    /**
     * 审核时间
     **/
    private Date auditDate;

    /**
     * 当前审批节点
     **/
    private String currentAuditNode;

    /**
     * 当前审批节点人和电话号码
     **/
    private String currentAuditPer;

    /**
     * 流程实例ID
     **/
    private String procInsId;

    /**
     * 流程类型:
     **/
    private String type;

    private String branchName;

    /**
     * 流程申请名称
     **/
    private String processApply;
    private String searchWord;

    private String idStr;

    private String comment;

    /**
     * 标题中需要加亮的字符串, 用竖线|隔开
     */
    private String highlight;

    /**
     * 流程节点类型
     */
    private String actNodeType;

    /**
     * 当前审核人，后台用户为登录名，app用户为用户id
     */
    private String assignee;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 流程发起人名称
     */
    private String starName;

    /**
     * 流程节点ID
     */
    private String actNodeId;

    /**
     * 流程定义id
     */
    private String procDefId;

    private List<String> processTypeList;

    private String officeId;

    /**
     * 是否自定义流程,0-否，1-是
     */
    private Integer customProcess;

    /**
     * 转交文案
     */
    private String transportContent;

    private List<ActivitiListener> activitiListenerList;
}
