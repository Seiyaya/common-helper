package xyz.seiyaya.stock.bean;

import lombok.Builder;
import lombok.Data;
import xyz.seiyaya.fund.bean.HistFundAccount;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 历史股票信息，涵盖了指数
 * @author seiyaya
 * @date 2020/1/21 10:44
 */
@Data
@Table(name = "t_hist_stock_info")
@Builder
public class HistStockInfo{

    @Id
    private Long id;
    private String marketId;
    private String stockCode;
    private Integer createDate;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    /**
     * 涨跌幅
     */
    private Double increase;
    /**
     * 涨跌
     */
    private Double dayGain;

    /**
     * 成交量
     */
    private Integer tradeQuantity;

    /**
     * 成交额
     */
    private Double tradeMoney;

    /**
     * 均线
     */
    private Double ma5;
    private Double ma10;
    private Double ma20;
    private Double ma60;


    public HistStockInfo() {
    }

    public HistStockInfo(String marketId, String stockCode, Integer createDate, Double open, Double close, Double high, Double low, Double increase, Double dayGain, Integer tradeQuantity, Double tradeMoney, Double ma5, Double ma10, Double ma20, Double ma60) {
        this.marketId = marketId;
        this.stockCode = stockCode;
        this.createDate = createDate;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.increase = increase;
        this.dayGain = dayGain;
        this.tradeQuantity = tradeQuantity;
        this.tradeMoney = tradeMoney;
        this.ma5 = ma5;
        this.ma10 = ma10;
        this.ma20 = ma20;
        this.ma60 = ma60;
    }
}
