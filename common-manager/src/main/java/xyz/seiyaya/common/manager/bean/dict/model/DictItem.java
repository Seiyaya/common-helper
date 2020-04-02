package xyz.seiyaya.common.manager.bean.dict.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/4/2 10:06
 */
@Table(name = "t_dict_value")
@Data
public class DictItem {

    @Id
    private String id;
    private String itemName;
    private String itemValue;
    /**
     * 0无效1有效
     */
    private Byte status;

    /**
     * 排序
     */
    private Integer sort;
}
