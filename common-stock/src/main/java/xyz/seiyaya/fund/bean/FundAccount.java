package xyz.seiyaya.fund.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 11:25
 */
@Data
@Table(name = "t_fund_account")
public class FundAccount {

    @Id
    private Integer id;
    private BigDecimal currentBalance;
    private BigDecimal totalBalance;
    private BigDecimal totalMarket;
    private Date createDate;
}
