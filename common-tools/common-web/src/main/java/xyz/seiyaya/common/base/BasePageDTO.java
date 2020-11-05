package xyz.seiyaya.common.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页参数的入参
 * @author wangjia
 * @version 1.0
 * @date 2020/6/22 17:39
 */
@Data
public class BasePageDTO implements Serializable {
    private Integer pageNum;
    private Integer pageSize;
}
