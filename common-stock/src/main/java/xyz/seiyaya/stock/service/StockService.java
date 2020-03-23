package xyz.seiyaya.stock.service;

import xyz.seiyaya.stock.bean.Stock;
import xyz.seiyaya.stock.bean.dto.StockDto;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/18 16:42
 */
public interface StockService {

    List<Stock> findByCondition(StockDto stockDto);
}
