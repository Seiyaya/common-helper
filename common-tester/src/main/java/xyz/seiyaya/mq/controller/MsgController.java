package xyz.seiyaya.mq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.mq.config.RabbitConfig;
import xyz.seiyaya.mq.service.MqService;
import xyz.seiyaya.mybatis.bean.UserBean;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/10 10:22
 */
@RestController
@RequestMapping("/msg")
public class MsgController {

    @Autowired
    private MqService mqService;

    @RequestMapping("/send")
    public ResultBean sendMsg(@RequestBody UserBean userBean){
        mqService.sendMsg(RabbitConfig.EXCHANGE_TRADE,null,userBean);
        return new ResultBean();
    }

    @RequestMapping("/sendRepeatMsg")
    public ResultBean sendRepeatMsg(@RequestBody UserBean userBean){
        mqService.sendMsg(RabbitConfig.ROUTING_KEY_TRADE_SEND_MSG,userBean);
        return new ResultBean();
    }
}
