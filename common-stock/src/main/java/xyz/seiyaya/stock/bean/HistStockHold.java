package xyz.seiyaya.stock.bean;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Table;

/**
 * 历史持仓信息
 * @author seiyaya
 * @date 2020/1/22 17:46
 */
@Data
@Table(name = "t_hist_stock_hold")
@Builder
public class HistStockHold extends StockHold{
}
