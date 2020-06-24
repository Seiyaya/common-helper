package xyz.seiyaya.activiti.bean.dto;

import lombok.Data;
import xyz.seiyaya.common.base.BasePageDTO;

/**
 * 流程搜索入参
 * @author wangjia
 * @version 1.0
 * @date 2020/6/22 17:38
 */
@Data
public class ProcessSearchDTO extends BasePageDTO {

    private Long userId;

    /**
     * 是否是已完成的流程0不是1是
     */
    private Byte complete;
}
