package xyz.seiyaya.activiti.service;

import xyz.seiyaya.activiti.bean.ActProcess;
import xyz.seiyaya.activiti.bean.AuditProcess;
import xyz.seiyaya.activiti.bean.DeployProcess;
import xyz.seiyaya.activiti.bean.dto.ProcessSearchDTO;
import xyz.seiyaya.activiti.bean.dto.StartProcessDTO;
import xyz.seiyaya.common.bean.ResultBean;

import java.io.IOException;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/2 14:42
 */
public interface ProcessService {

    /**
     * 部署流程信息
     * @param deployProcess
     * @throws IOException
     */
    void deployProcess(DeployProcess deployProcess) throws IOException;

    /**
     * 审核流程
     * @param auditProcess
     */
    void auditProcess(AuditProcess auditProcess);

    /**
     * 发起流程
     * @param startProcess
     * @return
     */
    ResultBean startProcess(StartProcessDTO startProcess);

    /**
     * 获取流程列表
     * @param processSearchDTO
     * @return
     */
    List<ActProcess> getProcessList(ProcessSearchDTO processSearchDTO);
}
