package xyz.seiyaya.activiti.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.seiyaya.activiti.bean.AuditProcess;
import xyz.seiyaya.activiti.bean.DeployProcess;
import xyz.seiyaya.activiti.service.ProcessService;
import xyz.seiyaya.common.bean.ResultBean;

import java.io.IOException;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/2 14:29
 */
@RestController
@RequestMapping("process")
@Slf4j
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @PostMapping("/deploy")
    public ResultBean deploy(MultipartFile xmlResource, MultipartFile pngResource,String name) throws IOException {
        DeployProcess deployProcess = new DeployProcess(xmlResource,pngResource,name);
        processService.deployProcess(deployProcess);
        return new ResultBean();
    }

    @RequestMapping("/audit")
    public ResultBean audit(AuditProcess auditProcess){
        processService.auditProcess(auditProcess);
        return new ResultBean();
    }

    @RequestMapping("/revoke")
    public ResultBean revoke(){
        return new ResultBean();
    }

    @RequestMapping("/list")
    public ResultBean list(){
        return new ResultBean();
    }

    @RequestMapping("/todoList")
    public ResultBean todoList(){
        return new ResultBean();
    }

    @RequestMapping("/doneList")
    public ResultBean doneList(){
        return new ResultBean();
    }

    @RequestMapping("/copyMeList")
    public ResultBean copyMeList(){
        return new ResultBean();
    }

    @RequestMapping("/transfer")
    public ResultBean transfer(){
        return new ResultBean();
    }

    @RequestMapping("/delete")
    public ResultBean delete(){
        return new ResultBean();
    }

    @RequestMapping("/stop")
    public ResultBean stop(){
        return new ResultBean();
    }

    @RequestMapping("/detail")
    public ResultBean detail(){
        return new ResultBean();
    }
}
