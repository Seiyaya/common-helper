package xyz.seiyaya.fund.bean;

import lombok.Data;

import javax.persistence.Table;

/**
 * 历史基金持仓
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 11:27
 */
@Data
@Table(name = "t_hist_fund_hold")
public class HistFundHold extends FundHold {

    /**
     * 备份日期，这个不需要参与计算或者其他，直接用数值表示更方便
     */
    private Integer backupDate;
}
