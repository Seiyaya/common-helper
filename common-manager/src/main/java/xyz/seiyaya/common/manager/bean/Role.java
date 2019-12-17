package xyz.seiyaya.common.manager.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 16:33
 */
@Table(name = "t_manager_role")
@Data
public class Role {

    @Id
    private Long id;
    private String role;
    private String remark;

    private Long createUserId;
    private Date createDate;
    private Long updateUserId;
    private Date updateDate;

    /**
     * 权限的集合
     */
    private List<Permission> permissionList;
}
