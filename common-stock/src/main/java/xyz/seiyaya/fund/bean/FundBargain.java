package xyz.seiyaya.fund.bean;

import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易的流水信息
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 11:33
 */
@Data
@Table(name = "t_fund_bargain")
public class FundBargain {

    private Integer id;
    private Integer accountId;
    private Integer holdId;
    /**
     * 交易数量
     */
    private BigDecimal num;
    /**
     * 交易净值
     */
    private BigDecimal price;
    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;
    /**
     * 交易类型
     * 0买入1卖出
     */
    private Integer tradeType;

    private String code;
    private String name;
    /**
     * 成交状态0未处理1成交
     */
    private Integer status;

    private Date createDate;

    public boolean isBuy(){
        return Integer.valueOf(0).equals(tradeType);
    }
}
