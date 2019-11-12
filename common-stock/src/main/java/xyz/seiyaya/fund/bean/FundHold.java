package xyz.seiyaya.fund.bean;

import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 基金持仓
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 11:23
 */
@Data
@Table(name = "t_fund_hold")
public class FundHold {
    private Integer id;
    private Integer accountId;
    private String code;
    private BigDecimal num;
    private BigDecimal costPrice;
    private Date createDate;
    private Date updateDate;

    public FundHold(){

    }

    public FundHold(FundBargain fundBargain){
        this.accountId = fundBargain.getAccountId();
        this.code = fundBargain.getCode();
        this.num = fundBargain.getNum();
        this.costPrice = fundBargain.getPrice();
        this.createDate = this.updateDate = new Date();
    }

    public FundHold(Integer id,BigDecimal num){
        this.id = id;
        this.num = num;
    }
}
