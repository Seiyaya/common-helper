package xyz.seiyaya.activiti.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.activiti.bean.ActProcess;
import xyz.seiyaya.activiti.service.ProcessService;
import xyz.seiyaya.common.bean.ResultBean;

import javax.annotation.Resource;

/**
 * @author wangjia
 * @version 1.0
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private ProcessService processService;

    @RequestMapping("/mybatis")
    public ResultBean testMybatis(@RequestBody ActProcess actProcess){
        processService.update(actProcess);
        return new ResultBean();
    }

    @RequestMapping("/valid")
    public ResultBean testValid(@RequestBody ActProcess actProcess){
        ResultBean resultBean = new ResultBean();
        resultBean.setData(actProcess);
        return resultBean;
    }
}
