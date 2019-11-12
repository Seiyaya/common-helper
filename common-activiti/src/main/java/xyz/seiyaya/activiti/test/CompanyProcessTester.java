package xyz.seiyaya.activiti.test;

import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.task.Task;
import org.assertj.core.util.Lists;
import org.junit.Test;
import xyz.seiyaya.activiti.bean.ActProcess;
import xyz.seiyaya.activiti.mapper.ActProcessMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公司流程框架的一些测试
 * @author wangjia
 * @version 1.0
 * @date 2019/11/11 16:17
 */
@Slf4j
public class CompanyProcessTester {

    private static ProcessEngine processEngine = null;

    private static TaskService taskService;

    private static RepositoryService repositoryService;

    static{
        processEngine = ProcessEngines.getDefaultProcessEngine();
        taskService = processEngine.getTaskService();
        repositoryService = processEngine.getRepositoryService();
    }

    /**
     * 测试转交，转交的过程中会查询当前结点
     */
    @Test
    public void testTurnOver(){
        //select distinct RES.* from ACT_RU_TASK RES WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc
        String proInsId = "dc46124404ca41459dff5b7abf96d186";
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(proInsId).orderByTaskId().asc().list();

        for(Task task : taskList){
            System.out.println(task+"-->"+ task.getAssignee());
        }
    }

    /**
     * 获取历史流程图
     */
    @Test
    public void getHistFollow(){
        String procInsId = "8dbed4c6a83a418cb7dc47e5b369cd24";
        List<ActProcess> actProcesses = processEngine.getManagementService().executeCustomSql(new AbstractCustomSqlExecution<ActProcessMapper, List<ActProcess>>(ActProcessMapper.class) {
            @Override
            public List<ActProcess> execute(ActProcessMapper actProcessMapper) {
                return actProcessMapper.getAuditProcessList(procInsId);
            }
        });

        if(actProcesses.isEmpty()){
           log.error("没有对应的流程结点信息");
        }
        String processDefId = actProcesses.get(0).getProcDefId();
        log.info("流程定义信息:{}",processDefId);
        StringBuilder nodeListStr = new StringBuilder("\n");
        for(ActProcess actProcess : actProcesses){
            String alreadyNode = String.format("itemStartName:%s --> nodeType:%s --> assignee:%s",actProcess.getStarName(),actProcess.getActNodeType(),actProcess.getAssignee());
            nodeListStr.append(alreadyNode).append("\n");
        }
        log.info("已经审核过的节点:{}",nodeListStr);
        for(ActProcess actProcess : actProcesses){
            if("startEvent".equals(actProcess.getActNodeType())) {
                log.info("startEvent[assignee:{} --> startName:{}]", actProcess.getAssignee(), actProcess.getStarName());
            }else if("endEvent".equals(actProcess.getActNodeType())){
                log.info("endEvent[currentAuditPer:{}]",actProcess.getCurrentAuditPer());
            }else if("userTask".equals(actProcess.getActNodeType())){

            }else if("serviceTask".equals(actProcess.getActNodeType())){

            }
        }

        actProcesses = actProcesses.stream().filter(model ->
            "userTask".equals(model.getActNodeType()) || "endEvent".equals(model.getActNodeType()) || "startEvent".equals(model.getActNodeType())
                ).collect(Collectors.toList());

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefId);
        Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
        //用来判断是否达到了正在执行的那一步
        boolean flag = false;
        ActProcess lastProcessNode = actProcesses.get(actProcesses.size() - 1);
        for(FlowElement flowElement : flowElements){
            String actNodeType = flowElement.getClass().getSimpleName();
            if("UserTask".equals(actNodeType) || "EndEvent".equals(actNodeType)){
                if(flag){
                    ActProcess actProcess = new ActProcess();
                    actProcess.setCurrentAuditNode(flowElement.getName());
                    actProcess.setActNodeId(flowElement.getId());
                    actProcess.setActNodeType(actNodeType);
                    if(flowElement instanceof UserTask){
                        List<ActivitiListener> taskListeners = ((UserTask) flowElement).getTaskListeners();
                        actProcess.setActivitiListenerList(taskListeners);
                    }
                    actProcesses.add(actProcess);
                    if("EndEvent".equals(actNodeType)){
                        break;
                    }
                }

                //先找到流程图在上面流程执行到了哪一步
                if(flowElement.getId().equals(lastProcessNode.getActNodeId())){
                    flag = true;
                }
            }
        }


        for(ActProcess actProcess : actProcesses){
            System.out.println(String.format("currentAuditNode:%s --> actNodeType:%s -->  actNodeId:%s --> listener:%s",
                    actProcess.getCurrentAuditNode(),actProcess.getActNodeType(),actProcess.getActNodeId(),actProcess.getActivitiListenerList()));
        }
    }
}
