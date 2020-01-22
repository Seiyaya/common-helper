package xyz.seiyaya.stock.bean;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Table;

/**
 * @author seiyaya
 * @date 2020/1/22 17:42
 */
@Data
@Table(name = "t_stock_bargain")
@Builder
public class StockBargain {
}
