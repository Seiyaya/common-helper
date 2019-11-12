package xyz.seiyaya.fund.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 10:41
 */
@Data
public class FundUser {

    private Integer id;
    private String username;
    private String password;
    private Date createDate;
    private Date updateDate;
}
