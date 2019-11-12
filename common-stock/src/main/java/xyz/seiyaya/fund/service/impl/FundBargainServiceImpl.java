package xyz.seiyaya.fund.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.common.bean.SearchBean;
import xyz.seiyaya.common.exception.ParamsException;
import xyz.seiyaya.fund.bean.FundBargain;
import xyz.seiyaya.fund.bean.FundHold;
import xyz.seiyaya.fund.bean.dto.FundBargainDto;
import xyz.seiyaya.fund.mapper.FundBargainMapper;
import xyz.seiyaya.fund.mapper.FundHoldMapper;
import xyz.seiyaya.fund.service.FundBargainService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 14:00
 */
@Service
@Transactional
public class FundBargainServiceImpl implements FundBargainService {

    @Resource
    private FundBargainMapper fundBargainMapper;

    @Resource
    private FundHoldMapper fundHoldMapper;

    @Override
    public List<FundBargain> findBargain(Integer accountId, SearchBean searchBean) {
        PageHelper.startPage(searchBean.getCurrentPage(),searchBean.getPageSize());

        FundBargainDto fundBargainDto = new FundBargainDto();
        BeanUtils.copyProperties(searchBean,fundBargainDto);
        List<FundBargain> fundBargain = fundBargainMapper.findFundBargain(fundBargainDto);
        return fundBargain;
    }

    @Override
    public void addBargain(FundBargain fundBargain) {
        /**
         * 添加成交
         * 判断是建仓还是加仓(减仓),建仓需要判断是部分减仓还是清仓
         */
        FundHold queryParams = new FundHold();
        queryParams.setCode(fundBargain.getCode());
        queryParams.setAccountId(fundBargain.getAccountId());
        FundHold fundHold = fundHoldMapper.selectOne(queryParams);
        if(fundHold == null){
            //建仓
            fundHold = new FundHold(fundBargain);
            fundHoldMapper.insertSelective(fundHold);
        }else{
            //加仓或者减仓
            if(fundBargain.isBuy()){
                FundHold updateFundHold = new FundHold(fundHold.getId(),fundHold.getNum().add(fundBargain.getNum()));
                updateFundHold.setUpdateDate(new Date());
                fundHoldMapper.updateByPrimaryKey(updateFundHold);
            }else{
                //减仓需要判断是否清仓
                if(fundBargain.getNum().compareTo(fundHold.getNum()) > 0){
                    throw new ParamsException("没有对应的持仓份数");
                }
                if(fundBargain.getNum().equals(fundHold.getNum())){
                    fundHoldMapper.deleteByPrimaryKey(fundHold.getId());
                }else{
                    FundHold updateFundHold = new FundHold(fundHold.getId(),fundHold.getNum().subtract(fundBargain.getNum()));
                    updateFundHold.setUpdateDate(new Date());
                    fundHoldMapper.updateByPrimaryKey(updateFundHold);
                }
            }
        }
        fundBargain.setHoldId(fundHold.getId());
        fundBargainMapper.insertSelective(fundBargain);
    }
}
