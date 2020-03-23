package xyz.seiyaya.stock.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.seiyaya.common.helper.HttpHelper;
import xyz.seiyaya.stock.bean.Stock;
import xyz.seiyaya.stock.mapper.StockMapper;

import java.math.BigDecimal;

/**
 * 更新历史数据task
 * @author wangjia
 * @version 1.0
 * @date 2020/3/20 14:13
 */
@Component
public class StockDataTask {

    @Autowired
    private StockMapper stockMapper;

    /**
     * 更新历史行情数据,每天四点更新
     */
    @Scheduled(cron="0 0 16 * * ?")
    public void updateHistoryData(){
        HttpHelper httpUtils = HttpHelper.getHttpUtils();
        String stockCode = "000001";
        String marketId = "SH";
        int count = 1;
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

            stock.setMa5(array.getBigDecimal(10).divide(scale , 2,BigDecimal.ROUND_HALF_UP));
            stock.setMa10(array.getBigDecimal(11).divide(scale , 2,BigDecimal.ROUND_HALF_UP));
            stock.setMa20(array.getBigDecimal(12).divide(scale , 2,BigDecimal.ROUND_HALF_UP));
            stock.setMa60(array.getBigDecimal(13).divide(scale , 2,BigDecimal.ROUND_HALF_UP));

            stockMapper.insert(stock);
        });
    }
}
