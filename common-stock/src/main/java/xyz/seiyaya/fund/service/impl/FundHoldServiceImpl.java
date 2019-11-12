package xyz.seiyaya.fund.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.fund.bean.FundHold;
import xyz.seiyaya.fund.bean.FundInfo;
import xyz.seiyaya.fund.bean.vo.FundHoldVo;
import xyz.seiyaya.fund.helper.QuotesCacheHelper;
import xyz.seiyaya.fund.mapper.FundHoldMapper;
import xyz.seiyaya.fund.service.FundHoldService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 10:53
 */
@Service
@Transactional
public class FundHoldServiceImpl implements FundHoldService {


    @Autowired
    private QuotesCacheHelper quotesCacheHelper;

    @Resource
    private FundHoldMapper fundHoldMapper;

    @Override
    public List<FundHoldVo> findHoldWithQuotes(Integer accountId) {
        List<FundHold> holdList = findHold(accountId);
        List<FundHoldVo> holdVoList = new ArrayList<>(holdList.size());
        List<String> codeList = holdList.stream().map(FundHold::getCode).collect(Collectors.toList());
        Map<String, FundInfo> fundInfoMap = quotesCacheHelper.getFundInfoMap(codeList);
        holdList.forEach(model->{
            FundInfo fundInfo = fundInfoMap.get(model.getCode());

            FundHoldVo fundHoldVo = new FundHoldVo();
            BeanUtils.copyProperties(model,fundHoldVo);

            fundHoldVo.setNowPrice(fundInfo.getPrice());
            fundHoldVo.setYesterdayPrice(fundInfo.getYesterdayPrice());
            fundHoldVo.setTotalEarn(fundHoldVo.calcTotalEarn());
            fundHoldVo.setDayEarn(fundHoldVo.calcDayEarn());
            fundHoldVo.setDayGain(fundHoldVo.calcDayGain());
            fundHoldVo.setFundName(fundInfo.getName());
            holdVoList.add(fundHoldVo);
        });
        holdList.clear();
        return holdVoList;
    }

    @Override
    public List<FundHold> findHold(Integer accountId) {
        FundHold queryParams = new FundHold();
        queryParams.setAccountId(accountId);
        return fundHoldMapper.select(queryParams);
    }
}
