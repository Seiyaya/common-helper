package xyz.seiyaya.quotes.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 10:30
 */
@Data
public class Fund {

    private String name;
    private Date updateDate;
    /**
     * 今日估值
     */
    private BigDecimal nowPrice;
    /**
     * 昨日净值
     */
    private BigDecimal yesterdayPrice;
    /**
     * 编码
     */
    private String code;

    public Fund(String code){
        this.code = code;
    }
}
