package xyz.seiyaya.activiti.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.seiyaya.activiti.bean.AuditProcess;
import xyz.seiyaya.activiti.bean.DeployProcess;
import xyz.seiyaya.activiti.bean.dto.StartProcessDTO;
import xyz.seiyaya.activiti.service.ProcessService;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.exception.ParamsException;
import xyz.seiyaya.common.msg.bean.ProcessMsg;
import xyz.seiyaya.common.msg.config.MsgConstant;
import xyz.seiyaya.common.msg.service.MsgService;

import java.io.IOException;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/2 14:42
 */
@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private MsgService msgService;

    @Override
    public void deployProcess(DeployProcess deployProcess) throws IOException {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().
                addInputStream(deployProcess.getXmlResource().getOriginalFilename(), deployProcess.getXmlResource().getInputStream());
        if(deployProcess.getPngResource() != null){
            deploymentBuilder.addInputStream(deployProcess.getPngResource().getOriginalFilename(),deployProcess.getXmlResource().getInputStream());
        }
        deploymentBuilder.deploy();
    }

    @Override
    public void auditProcess(AuditProcess auditProcess) {
        Task task = taskService.createTaskQuery().processInstanceId(auditProcess.getProcInsId()).taskAssignee(String.valueOf(auditProcess.getUserId())).singleResult();
        if(task == null){
            log.info("不存在对应的任务或者重复审核  serialNo:{} -->  procInsId:{}",auditProcess.getSerialNo(),auditProcess.getProcInsId());
            throw new ParamsException("不存在对应的任务或者重复审核");
        }
    }

    @Override
    public ResultBean startProcess(StartProcessDTO startProcess) {
        ResultBean resultBean = new ResultBean();
        // 校验流程是否存在
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(startProcess.getProcessType()).singleResult();
        if(processDefinition == null){
            return resultBean.setParamError("流程定义不存在");
        }
        // 校验流程是否是已有流程
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processDefinitionKey(startProcess.getProcessType()).processInstanceBusinessKey(startProcess.getSerialNo()).singleResult();
        if(instance != null){
            return resultBean.setParamError("已存在对应流程:{}",startProcess.getSerialNo());
        }
        // 设置身份信息并发起流程
        identityService.setAuthenticatedUserId(String.valueOf(startProcess.getUserId()));
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(startProcess.getProcessType(), startProcess.getSerialNo(), startProcess.getParams());
        if(processInstance == null){
            log.error("start process error , process info : {}", JSON.toJSONString(startProcess));
            return resultBean.setError("流程发起失败");
        }

        // 同步自身业务系统的表数据


        ProcessMsg processMsg = new ProcessMsg();
        processMsg.setUserId(startProcess.getUserId());
        processMsg.setProcessType(startProcess.getProcessType());
        processMsg.setSerialNo(startProcess.getSerialNo());
        msgService.sendMsg(MsgConstant.PROCESS, processMsg);
        return resultBean;
    }
}
