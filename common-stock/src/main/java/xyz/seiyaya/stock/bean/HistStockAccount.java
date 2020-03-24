package xyz.seiyaya.stock.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 历史账户
 * @author seiyaya
 * @date 2020/1/22 17:46
 */
@Data
@Table(name = "t_hist_stock_account")
public class HistStockAccount {

    @Id
    private Long Id;
}
