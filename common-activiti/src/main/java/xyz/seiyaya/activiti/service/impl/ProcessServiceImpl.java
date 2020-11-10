package xyz.seiyaya.activiti.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.activiti.bean.ActProcess;
import xyz.seiyaya.activiti.bean.AuditProcess;
import xyz.seiyaya.activiti.bean.DeployProcess;
import xyz.seiyaya.activiti.bean.dto.ProcessSearchDTO;
import xyz.seiyaya.activiti.bean.dto.StartProcessDTO;
import xyz.seiyaya.activiti.bean.vo.ActProcessVO;
import xyz.seiyaya.activiti.mapper.ActProcessMapper;
import xyz.seiyaya.activiti.service.ProcessService;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.constant.Constant;
import xyz.seiyaya.common.exception.ParamsException;
import xyz.seiyaya.common.helper.JSONHelper;
import xyz.seiyaya.common.helper.SnowflakeIdHelper;
import xyz.seiyaya.common.msg.bean.ProcessMsg;
import xyz.seiyaya.common.msg.config.MsgConstant;
import xyz.seiyaya.common.msg.service.MsgService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/2 14:42
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProcessServiceImpl implements ProcessService {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private IdentityService identityService;

    @Resource
    private TaskService taskService;

    @Resource
    private MsgService msgService;

    @Resource
    private ActProcessMapper actProcessMapper;

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
            log.error("start process error , process info : {}", JSONHelper.toJSONString(startProcess));
            return resultBean.setError("流程发起失败");
        }

        // 同步自身业务系统的表数据
        ActProcess actProcess = ActProcess.builder()
                .id(SnowflakeIdHelper.nextId())
                .serialNo(startProcess.getSerialNo())
                .processTitle(startProcess.getProcessTitle())
                .createDate(new Date())
                .createUserId(startProcess.getUserId())
                .auditStatus(Constant.ByteConstant.BYTE_0)
                .procInstId(processInstance.getId())
                .type(startProcess.getProcessType())
                .procDefId(processInstance.getProcessDefinitionId())
                .build();
        actProcessMapper.insert(actProcess);


        ProcessMsg processMsg = new ProcessMsg();
        processMsg.setUserId(startProcess.getUserId());
        processMsg.setProcessType(startProcess.getProcessType());
        processMsg.setSerialNo(startProcess.getSerialNo());
        msgService.sendMsg(MsgConstant.PROCESS, processMsg);
        return resultBean;
    }

    @Override
    public List<ActProcess> getProcessList(ProcessSearchDTO processSearchDTO) {
        PageHelper.startPage(processSearchDTO.getPageNum(),processSearchDTO.getPageSize());
        return null;
    }

    @Override
    public PageInfo<ActProcessVO> getTodoProcessList(ProcessSearchDTO processSearchDTO) {
        PageHelper.startPage(processSearchDTO.getPageNum(),processSearchDTO.getPageSize());
        List<ActProcessVO> list = actProcessMapper.getTodoProcessList(processSearchDTO);
        PageInfo<ActProcessVO> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public PageInfo<ActProcessVO> getDoneProcessList(ProcessSearchDTO processSearchDTO) {
        PageHelper.startPage(processSearchDTO.getPageNum(),processSearchDTO.getPageSize());
        List<ActProcessVO> list = actProcessMapper.getTodoProcessList(processSearchDTO);
        PageInfo<ActProcessVO> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public void update(ActProcess actProcess) {
        ActProcess dbProcess = actProcessMapper.selectByPrimaryKey(actProcess.getId());
        log.info("first query :{}   name:{}",dbProcess,dbProcess.getProcessTitle());

        dbProcess.setProcessTitle("0.0 = =");

        ActProcess dbProcess1 = actProcessMapper.selectByPrimaryKey(actProcess.getId());
        log.info("two query :{}   name:{}",dbProcess1,dbProcess1.getProcessTitle());

        log.info("equals :{}", dbProcess == dbProcess1);
    }
}
