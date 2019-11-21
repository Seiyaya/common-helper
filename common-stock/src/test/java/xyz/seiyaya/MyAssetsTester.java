package xyz.seiyaya;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.fund.FundInfoApplication;
import xyz.seiyaya.fund.bean.vo.FundHoldVo;
import xyz.seiyaya.fund.service.FundHoldService;

import java.math.BigDecimal;
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

    @Test
    public void testAssets(){
        List<FundHoldVo> holdWithQuotes = fundHoldService.findHoldWithQuotes(1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(FundHoldVo fundHoldVo : holdWithQuotes){
            BigDecimal marketPrice = fundHoldVo.calcMarketPrice();
            log.info("基金名称:{}  市场价:{}   持仓市值:{}",fundHoldVo.getFundName(),fundHoldVo.getNowPrice(),marketPrice);
            totalAmount = totalAmount.add(marketPrice);
        }

        log.info("totalAmount : {}",totalAmount);
    }
}
