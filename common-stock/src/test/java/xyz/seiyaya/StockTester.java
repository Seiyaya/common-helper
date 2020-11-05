package xyz.seiyaya;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.common.cache.helper.HttpHelper;
import xyz.seiyaya.common.cache.helper.SpringHelper;
import xyz.seiyaya.common.config.MybatisInterceptorConfig;
import xyz.seiyaya.stock.StockApplication;
import xyz.seiyaya.stock.bean.Stock;
import xyz.seiyaya.stock.bean.dto.StockDto;
import xyz.seiyaya.stock.mapper.StockMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/18 15:32
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = StockApplication.class)
@Slf4j
public class StockTester {

    @Resource
    private StockMapper stockMapper;

    @Test
    public void collectionHistoryData() {
        HttpHelper httpUtils = HttpHelper.getHttpUtils();
        String stockCode = "399975";
        String marketId = "SZ";
        int count = 2000;
        String result = httpUtils.sendGet(String.format("http://www.seiyaya.com:8887/market/json?funcno=20029&version=1&stock_code=%s&market=%s&type=day&count="+count, stockCode, marketId));
        JSONObject topObject = JSONObject.parseObject(result);
        JSONArray results = topObject.getJSONArray("results");
        BigDecimal scale = new BigDecimal(100);
        results.forEach(model -> {
            JSONArray array = (JSONArray) model;
            Stock stock = new Stock();
            stock.setMarket(marketId);
            stock.setCode(stockCode);
            stock.setCreateDate(array.getIntValue(0));
            stock.setOpen(array.getBigDecimal(1).divide(scale, 2,BigDecimal.ROUND_HALF_UP));
            stock.setHigh(array.getBigDecimal(2).divide(scale, 2,BigDecimal.ROUND_HALF_UP));
            stock.setClose(array.getBigDecimal(3).divide(scale, 2,BigDecimal.ROUND_HALF_UP));
            stock.setLow(array.getBigDecimal(4).divide(scale, 2,BigDecimal.ROUND_HALF_UP));
            stock.setIncrease(array.getBigDecimal(5).multiply(scale));
            stock.setUpDown(array.getBigDecimal(6));
            stock.setVolume(array.getBigDecimal(8).longValue());
            stock.setTurnover(array.getBigDecimal(9).longValue());

            stock.setMa5(array.getBigDecimal(10).divide(scale , 2,BigDecimal.ROUND_HALF_UP));
            stock.setMa10(array.getBigDecimal(11).divide(scale , 2,BigDecimal.ROUND_HALF_UP));
            stock.setMa20(array.getBigDecimal(12).divide(scale , 2,BigDecimal.ROUND_HALF_UP));
            stock.setMa60(array.getBigDecimal(13).divide(scale , 2,BigDecimal.ROUND_HALF_UP));

            stockMapper.insert(stock);
        });
    }


    @Test
    public void queryDrawData(){
        MybatisInterceptorConfig bean = SpringHelper.getBean(MybatisInterceptorConfig.class);
        System.out.println(bean);
        List<Stock> byCondition = stockMapper.findByCondition(StockDto.builder().startDate(20200301).endDate(20200318).build());

        byCondition.forEach(model -> log.info("{}",model));
    }

    public static void methodA(){
        Logger logger = LoggerFactory.getLogger(StockTester.class);
        logger.info("213");
    }

    public void methodB(){
        methodA();
    }

}
