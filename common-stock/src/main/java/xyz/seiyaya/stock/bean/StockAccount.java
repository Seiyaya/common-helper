package xyz.seiyaya.stock.bean;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Table;

/**
 * @author seiyaya
 * @date 2020/1/22 17:45
 */
@Data
@Table(name = "t_stock_account")
@Builder
public class StockAccount {
}
