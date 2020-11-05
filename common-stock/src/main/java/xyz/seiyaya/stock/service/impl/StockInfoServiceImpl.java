package xyz.seiyaya.stock.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.seiyaya.common.cache.helper.HttpHelper;
import xyz.seiyaya.stock.bean.HistStockInfo;
import xyz.seiyaya.stock.mapper.HistStockInfoMapper;
import xyz.seiyaya.stock.service.StockInfoService;

import javax.annotation.Resource;

/**
 * @author seiyaya
 * @date 2020/1/21 10:48
 */
@Service
@Slf4j
public class StockInfoServiceImpl implements StockInfoService {

    @Resource
    private HistStockInfoMapper histStockInfoMapper;

    @Override
    public void insertHistStockInfo(String marketId, String stockCode) {
        this.insertHistStockInfo(marketId,stockCode,99999);
    }

    @Override
    public void insertHistStockInfo(String marketId, String stockCode, int count) {
        // 校验历史行情信息是否存在
        HistStockInfo queryParam = new HistStockInfo();
        queryParam.setMarketId(marketId);
        queryParam.setStockCode(stockCode);
        int countResult = histStockInfoMapper.selectCount(queryParam);
        if(countResult > 0){
            log.info("{}:{} 历史行情信息已经存在:{}",marketId,stockCode,countResult);
            return ;
        }

        String result = HttpHelper.getHttpUtils().sendGet("http://www.hqserver.xyz:8887/market/json?funcno=20029&version=1&stock_code="+stockCode+"&market="+marketId+"&type=day&count="+count);
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
            double dayGain = currentArray.getDoubleValue(6);
            // 成交量 单位手
            int tradeQuantity = currentArray.getInteger(8);
            // 成交额  单位元
            double tradeMoney = currentArray.getDoubleValue(9) / 100;
            // 5日10日20日60日 均线
            double ma5 = currentArray.getDoubleValue(10) / 100;
            double ma10 = currentArray.getDoubleValue(11) / 100;
            double ma20 = currentArray.getDoubleValue(12) / 100;
            double ma60 = currentArray.getDoubleValue(13) / 100;

            HistStockInfo histStockInfo = new HistStockInfo(marketId,stockCode,createDate,open,close,high,low,increase,dayGain,tradeQuantity,tradeMoney,ma5,ma10,ma20,ma60);
            histStockInfoMapper.insertSelective(histStockInfo);
        });
    }
}
