package xyz.seiyaya.common.msg.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/6/19 9:58
 */
@Data
public abstract class JSONMsg implements Serializable {

    /**
     * 接收人id
     */
    private Long userId;

    public String toJSONString(){
        return JSON.toJSONString(this);
    }
}
