package xyz.seiyaya.stock.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.seiyaya.stock.bean.Stock;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/18 16:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDto extends Stock {

    private Integer startDate;

    private Integer endDate;
}
