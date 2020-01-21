package xyz.seiyaya;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.common.helper.HttpHelper;
import xyz.seiyaya.stock.bean.HistStockInfo;
import xyz.seiyaya.stock.mapper.HistStockInfoMapper;

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

    @Test
    public void testGetHistStockInfo(){
        int count =  99999;
        String result = HttpHelper.getHttpUtils().sendGet("http://www.hqserver.xyz:8887/market/json?funcno=20029&version=1&stock_code=000001&market=SH&type=day&count=" + count);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray stockCodeInfoArray = jsonObject.getJSONArray("results");
        stockCodeInfoArray.forEach(model->{
            JSONArray currentArray = (JSONArray)model;
            Integer createDate = currentArray.getInteger(0);
            double open = currentArray.getDoubleValue(1)/100;
            double high = currentArray.getDoubleValue(2)/100;
            double close = currentArray.getDoubleValue(3)/100;
            double low = currentArray.getDoubleValue(4)/100;
            // 涨跌幅，指的是百分比的增长
            double increase = currentArray.getDoubleValue(5) * 100;
            // 涨跌，指的是数值的增长
            double dayGain = currentArray.getDoubleValue(6) * 100;
            // 成交量 单位手
            int tradeQuantity = currentArray.getInteger(8);
            // 成交额  单位元
            double tradeMoney = currentArray.getDoubleValue(9) / 100;
            // 5日10日20日60日 均线
            double ma5 = currentArray.getDoubleValue(10) / 100;
            double ma10 = currentArray.getDoubleValue(11) / 100;
            double ma20 = currentArray.getDoubleValue(12) / 100;
            double ma60 = currentArray.getDoubleValue(13) / 100;

            HistStockInfo histStockInfo = new HistStockInfo("SH","000001",createDate,open,close,high,low,increase,dayGain,tradeQuantity,tradeMoney,ma5,ma10,ma20,ma60);
            log.info("histStockInfo:{}",histStockInfo);
            histStockInfoMapper.insertSelective(histStockInfo);
        });
    }
}
