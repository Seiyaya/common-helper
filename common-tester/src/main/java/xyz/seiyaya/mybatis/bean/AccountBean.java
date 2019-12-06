package xyz.seiyaya.mybatis.bean;

import lombok.Data;

/**
 * 用户下的交易账号
 * @author wangjia
 * @version 1.0
 * @date 2019/9/27 14:45
 */
@Data
public class AccountBean {

    private Integer id;
    private Integer userId;
    private double initBalance;
    private double totalBalance;
    private double currentBalance;
}
