package xyz.seiyaya.stock.bean;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 股票信息
 * @author seiyaya
 * @date 2020/1/22 17:39
 */
@Data
@Builder
@Table(name = "t_stock_info")
public class StockInfo {

    @Id
    private Long id;
    private String marketId;
    private String stockCode;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double now;
    /**
     * 涨幅
     */
    private Double increase;
    /**
     * 涨跌
     */
    private Double dayGain;
    private String pinyinName;


}
