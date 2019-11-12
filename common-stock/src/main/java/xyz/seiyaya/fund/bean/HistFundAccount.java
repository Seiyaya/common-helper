package xyz.seiyaya.fund.bean;

import lombok.Data;

import javax.persistence.Table;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 11:28
 */
@Data
@Table(name = "t_hist_fund_account")
public class HistFundAccount extends FundAccount {

    private Integer backupDate;
}
