package xyz.seiyaya.mybatis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.mybatis.bean.UserBean;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 9:28
 */
@RestController
@RequestMapping("/mybatis")
public class MybatisController extends BaseController {

    @GetMapping("/get")
    public ResultBean testGet(@RequestBody UserBean userBean){
        System.out.println(userBean);
        return new ResultBean();
    }
}
