package xyz.seiyaya.activiti.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.seiyaya.activiti.bean.AuditProcess;
import xyz.seiyaya.activiti.bean.DeployProcess;
import xyz.seiyaya.activiti.handler.ProcessHandler;
import xyz.seiyaya.activiti.service.ProcessService;
import xyz.seiyaya.common.exception.ParamsException;
import xyz.seiyaya.common.helper.ObjectHelper;
import xyz.seiyaya.common.helper.SpringHelper;

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
    private TaskService taskService;

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
        ProcessHandler processHandler = SpringHelper.getBean(auditProcess.getType(), ProcessHandler.class);
        String specialContent = processHandler.getSpecialContent();
        String content = (auditProcess.getAgree() ? "[同意] " : "[驳回] ") + specialContent + ObjectHelper.ifNull(auditProcess.getContent(),"");
    }
}
