package xyz.seiyaya.fund.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.seiyaya.common.bean.SearchBean;
import xyz.seiyaya.fund.bean.FundBargain;
import xyz.seiyaya.fund.bean.dto.FundBargainDto;
import xyz.seiyaya.fund.mapper.FundBargainMapper;
import xyz.seiyaya.fund.service.BargainService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 14:00
 */
@Service
public class BargainServiceImpl implements BargainService {

    @Resource
    private FundBargainMapper fundBargainMapper;

    @Override
    public List<FundBargain> findBargain(Integer accountId, SearchBean searchBean) {
        PageHelper.startPage(searchBean.getCurrentPage(),searchBean.getPageSize());

        FundBargainDto fundBargainDto = new FundBargainDto();
        BeanUtils.copyProperties(searchBean,fundBargainDto);
        List<FundBargain> fundBargain = fundBargainMapper.findFundBargain(fundBargainDto);
        return fundBargain;
    }
}
