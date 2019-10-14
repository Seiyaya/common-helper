package xyz.seiyaya.deploy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ResultBean;

/**
 * @author seiyaya
 * @date 2019/10/15 0:43
 */
@RequestMapping("/deploy")
@RestController
@Slf4j
public class DeployController extends BaseController {

    /**
     * 进行打包
     * @return
     */
    @RequestMapping("/package")
    public ResultBean packageJar(){
        try {
            Process exec = Runtime.getRuntime().exec("/data/project/ && ./build.sh");
            int status  = exec.waitFor();

            if(status != 0){
                log.error("调用shell命令失败:{}",status);
                return new ResultBean("调用shell命令失败",ResultBean.ResultConstant.CODE_ERROR.getCode());
            }
        } catch (Exception e) {
            log.error("更新git出现异常:",e);
        }
        return new ResultBean("执行成功");
    }
}
