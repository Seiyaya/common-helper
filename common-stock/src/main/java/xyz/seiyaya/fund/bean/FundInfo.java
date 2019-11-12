package xyz.seiyaya.fund.bean;

import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 11:21
 */
@Data
@Table(name = "t_fund_info")
public class FundInfo {

    private Integer id;
    private String code;
    private String name;
    private BigDecimal price;
    private BigDecimal yesterdayPrice;
    /**
     * 累计净值，不含包派息所减少的部分
     */
    private BigDecimal sumPrice;
    private Date updateDate;

    public FundInfo(){

    }

    public FundInfo(String code){
        this.code = code;
    }
}
