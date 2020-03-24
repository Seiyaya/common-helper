package xyz.seiyaya.stock.bean;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author seiyaya
 * @date 2020/1/22 17:40
 */
@Data
@Table(name = "t_stock_hold")
@Builder
public class StockHold {

    @javax.persistence.Id
    private Long Id;
}
