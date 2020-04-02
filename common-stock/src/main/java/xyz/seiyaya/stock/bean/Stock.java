package xyz.seiyaya.stock.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/18 14:38
 */
@Table(name = "t_stock_info")
@Data
public class Stock implements Serializable {

    @Id
    private Long id;

    private String market;

    private String code;

    private BigDecimal now;

    @Transient
    private String name;

    private BigDecimal open;

    private BigDecimal close;

    private BigDecimal high;

    private BigDecimal low;

    private BigDecimal increase;

    private BigDecimal upDown;

    private Integer createDate;

    private BigDecimal ma5;

    private BigDecimal ma10;

    private BigDecimal ma20;

    private BigDecimal ma60;

    /**
     * 成交量
     */
    private Long volume;

    /**
     * 成交额
     */
    private Long turnover;
}
