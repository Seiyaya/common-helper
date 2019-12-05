package xyz.seiyaya.common.bean;

import lombok.Data;

import javax.persistence.Table;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/27 11:37
 */
@Data
@Table(name = "t_dict")
public class Dict {

    private Integer id;
    /**
     * 大类，比如买卖类型，英文名称
     */
    private String parentType;
    /**
     * 字典key,比如0标识买入  此处0即为key
     */
    private Integer dictKey;
    /**
     * 字典value,比如0标识买入  此处买入即为value
     */
    private String dictValue;
}
