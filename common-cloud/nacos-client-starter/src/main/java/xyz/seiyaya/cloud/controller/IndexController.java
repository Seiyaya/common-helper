package xyz.seiyaya.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.bean.ResultBean;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/9 9:28
 */
@RestController
@Slf4j
public class IndexController {

    @GetMapping("/index")
    public ResultBean index(@RequestParam String name){
        log.info("invoked name :{}",name);
        return new ResultBean().setData(name);
    }
}
