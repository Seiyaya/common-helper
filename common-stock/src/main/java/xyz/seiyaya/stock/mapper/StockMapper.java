package xyz.seiyaya.stock.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.seiyaya.stock.bean.Stock;
import xyz.seiyaya.stock.bean.dto.StockDto;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/18 15:31
 */
@Mapper
public interface StockMapper extends tk.mybatis.mapper.common.Mapper<Stock> {

    /**
     * 通过制定条件查找股票信息
     * @param stockDto
     * @return
     */
    List<Stock> findByCondition(StockDto stockDto);
}
