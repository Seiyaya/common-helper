package xyz.seiyaya.fund.bean.vo;

import lombok.Data;
import xyz.seiyaya.fund.bean.FundHold;

import java.math.BigDecimal;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 10:56
 */
@Data
public class FundHoldVo extends FundHold {

    /**
     * 估值
     */
    private BigDecimal nowPrice;
    /**
     * 昨日净值
     */
    private BigDecimal yesterdayPrice;
    /**
     * 总盈亏
     */
    private BigDecimal totalEarn;
    /**
     * 日盈亏
     */
    private BigDecimal dayEarn;

    /**
     * 涨幅
     */
    private BigDecimal dayGain;

    private String fundName;

    public BigDecimal calcTotalEarn(){
        return nowPrice.subtract(getCostPrice()).multiply(getNum()).setScale(4,BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal calcDayEarn(){
        return nowPrice.subtract(yesterdayPrice).multiply(getNum()).setScale(4,BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal calcDayGain(){
        return nowPrice.subtract(yesterdayPrice).divide(yesterdayPrice,4,BigDecimal.ROUND_HALF_EVEN);
    }
}
