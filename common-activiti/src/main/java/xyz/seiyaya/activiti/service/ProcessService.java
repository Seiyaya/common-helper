package xyz.seiyaya.activiti.service;

import xyz.seiyaya.activiti.bean.AuditProcess;
import xyz.seiyaya.activiti.bean.DeployProcess;

import java.io.IOException;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/2 14:42
 */
public interface ProcessService {

    /**
     * 部署流程信息
     * @param deployProcess
     */
    void deployProcess(DeployProcess deployProcess) throws IOException;

    /**
     * 审核流程
     * @param auditProcess
     */
    void auditProcess(AuditProcess auditProcess);
}