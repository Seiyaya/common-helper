package xyz.seiyaya.mq.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/19 16:09
 */
@Data
public class MessageBean implements Serializable {

    private String requestId;

    private String msg;

    public MessageBean(){
        requestId = UUID.randomUUID().toString();
    }
}
