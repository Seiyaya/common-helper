package xyz.seiyaya.common.manager.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 权限
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 16:35
 */
@Table(name = "t_manager_permission")
@Data
public class Permission {

    @Id
    private Long id;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限标识
     */
    private String permission;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建日期
     */
    private Date createDate;
}
