package xyz.seiyaya.common.msg.processor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.common.msg.bean.ProcessMsg;

/**
 * 流程消息处理器
 * @author wangjia
 * @version 1.0
 */
@Slf4j
public class ProcessProcessor {

    public void startProcess(String msg){
        log.info("收到消息:{}", getProcessMsg(msg).toString());
    }


    public ProcessMsg getProcessMsg(String processMsg){
        return JSON.parseObject(processMsg, ProcessMsg.class);
    }
}
