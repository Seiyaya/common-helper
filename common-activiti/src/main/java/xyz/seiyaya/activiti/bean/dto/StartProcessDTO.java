package xyz.seiyaya.activiti.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 开启流程的入参
 * @author wangjia
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
public class StartProcessDTO implements Serializable {

    /**
     * 发起人id
     */
    private Long userId;
    /**
     * 发起人所在组织id
     */
    private Long teamId;
    /**
     * 流程类型
     */
    private String processType;
    /**
     * 流程标题
     */
    private String processTitle;
    /**
     * 流程编号
     */
    private String serialNo;
    /**
     * 流程审核中可能用到的参数列表
     */
    private Map<String,Object> params;
}
