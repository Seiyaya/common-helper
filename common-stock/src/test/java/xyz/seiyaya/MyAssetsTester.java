package xyz.seiyaya;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.provider.ExampleProvider;
import xyz.seiyaya.fund.FundInfoApplication;
import xyz.seiyaya.fund.bean.HistFundInfo;
import xyz.seiyaya.fund.bean.vo.FundHoldVo;
import xyz.seiyaya.fund.helper.QuotesHelper;
import xyz.seiyaya.fund.mapper.HistFundInfoMapper;
import xyz.seiyaya.fund.service.FundHoldService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/15 9:26
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = FundInfoApplication.class)
@Slf4j
public class MyAssetsTester {

    @Autowired
    private FundHoldService fundHoldService;

    @Autowired
    private QuotesHelper quotesHelper;

    @Resource
    private HistFundInfoMapper histFundInfoMapper;

    @Test
    public void testAssets(){
        List<FundHoldVo> holdWithQuotes = fundHoldService.findHoldWithQuotes(1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalDayEarn = BigDecimal.ZERO;
        for(FundHoldVo fundHoldVo : holdWithQuotes){
            BigDecimal marketPrice = fundHoldVo.calcMarketPrice();
            log.info("基金名称:{}  市场价:{}   持仓市值:{} 日盈亏:{}",fundHoldVo.getFundName(),fundHoldVo.getNowPrice(),marketPrice,fundHoldVo.getDayEarn());
            totalAmount = totalAmount.add(marketPrice);
            totalDayEarn = totalDayEarn.add(fundHoldVo.getDayEarn());
        }

        log.info("totalAmount : {}  totalDayEarn:{}",totalAmount,totalDayEarn);
    }

    @Test
    public void testInsertHistInfo(){
        List<HistFundInfo> histQuotes = quotesHelper.getHistQuotes("161725");
        histQuotes.forEach(model->{
            histFundInfoMapper.insertSelective(model);
        });
    }

    @Test
    public void testCalcHistDayEarn(){
        Example example = new Example(HistFundInfo.class);
        example.createCriteria().andEqualTo("code","161725");
        example.orderBy("backupDate").desc();
        List<HistFundInfo> histFundInfoList = histFundInfoMapper.selectByExample(example);
        for(int i=0;i<histFundInfoList.size()-1;i++){
            BigDecimal result = histFundInfoList.get(i).getPrice().divide(histFundInfoList.get(i+1).getPrice(),4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
            HistFundInfo histFundInfo = new HistFundInfo();
            histFundInfo.setId(histFundInfoList.get(i).getId());
            histFundInfo.setYesterdayPrice(histFundInfoList.get(i+1).getPrice());
            histFundInfo.setDayGain(result);
            histFundInfoMapper.updateByPrimaryKey(histFundInfo);
        }
        System.out.println(histFundInfoList);
    }
}
