package xyz.seiyaya;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.stock.mapper.HistStockInfoMapper;
import xyz.seiyaya.stock.service.StockInfoService;

import javax.annotation.Resource;

/**
 * 历史股票信息采集(包含指数信息)
 * @author seiyaya
 * @date 2020/1/21 10:16
 */
@Slf4j
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = FundInfoApplication.class)
public class HistStockInfoCollectionTest {

    @Resource
    private HistStockInfoMapper histStockInfoMapper;

    @Resource
    private StockInfoService stockInfoService;

    @Test
    public void testGetHistStockInfo(){
        String marketId = "SH";
        String stockCode = "000001";
        stockInfoService.insertHistStockInfo(marketId,stockCode);
    }
}
