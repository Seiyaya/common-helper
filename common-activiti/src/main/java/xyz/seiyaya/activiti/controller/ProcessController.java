package xyz.seiyaya.activiti.controller;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.seiyaya.activiti.bean.ActProcess;
import xyz.seiyaya.activiti.bean.AuditProcess;
import xyz.seiyaya.activiti.bean.DeployProcess;
import xyz.seiyaya.activiti.bean.dto.ProcessSearchDTO;
import xyz.seiyaya.activiti.bean.vo.ActProcessVO;
import xyz.seiyaya.activiti.service.ProcessService;
import xyz.seiyaya.common.annotation.LoginUser;
import xyz.seiyaya.common.bean.LoginUserInfo;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.constant.Constant;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/2 14:29
 */
@RestController
@RequestMapping("process")
@Slf4j
public class ProcessController {

    @Resource
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

    /**
     * 我的流程列表
     * @param processSearchDTO
     * @return
     */
    @RequestMapping("/list")
    public ResultBean list(@RequestBody ProcessSearchDTO processSearchDTO, @LoginUser LoginUserInfo loginUserInfo){
        ResultBean result = new ResultBean();
        processSearchDTO.setUserId(loginUserInfo.getId());
        List<ActProcess> list = processService.getProcessList(processSearchDTO);
        result.setData(list);
        return result;
    }

    @RequestMapping("/todoList")
    public ResultBean todoList(@RequestBody ProcessSearchDTO processSearchDTO, @LoginUser LoginUserInfo loginUserInfo){
        ResultBean resultBean = new ResultBean();
        processSearchDTO.setComplete(Constant.ByteConstant.BYTE_0);
        processSearchDTO.setUserId(loginUserInfo.getId());
        PageInfo<ActProcessVO> page = processService.getTodoProcessList(processSearchDTO);
        resultBean.setData(page);
        return resultBean;
    }

    @RequestMapping("/doneList")
    public ResultBean doneList(@RequestBody ProcessSearchDTO processSearchDTO, @LoginUser LoginUserInfo loginUserInfo){
        ResultBean resultBean = new ResultBean();
        processSearchDTO.setComplete(Constant.ByteConstant.BYTE_1);
        processSearchDTO.setUserId(loginUserInfo.getId());
        PageInfo<ActProcessVO> page = processService.getDoneProcessList(processSearchDTO);
        resultBean.setData(page);
        return resultBean;
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
