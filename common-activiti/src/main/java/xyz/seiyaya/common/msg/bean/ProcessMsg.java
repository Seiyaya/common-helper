package xyz.seiyaya.common.msg.bean;

import lombok.Data;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/6/19 9:18
 */
@Data
public class ProcessMsg extends JSONMsg {

    private String processType;
    private String serialNo;
}
