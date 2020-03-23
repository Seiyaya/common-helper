package xyz.seiyaya.stock.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.stock.bean.Stock;
import xyz.seiyaya.stock.bean.dto.StockDto;
import xyz.seiyaya.stock.mapper.StockMapper;
import xyz.seiyaya.stock.service.StockService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/18 16:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StockServiceImpl implements StockService {

    @Resource
    private StockMapper stockMapper;

    @Override
    public List<Stock> findByCondition(StockDto stockDto) {
        return stockMapper.findByCondition(stockDto);
    }
}
