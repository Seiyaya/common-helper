package xyz.seiyaya.fund.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.common.bean.SearchBean;
import xyz.seiyaya.fund.bean.HistFundInfo;
import xyz.seiyaya.fund.mapper.HistFundInfoMapper;
import xyz.seiyaya.fund.service.FundInfoService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 15:52
 */
@Service
@Transactional
public class FundInfoServiceImpl implements FundInfoService {

    @Resource
    private HistFundInfoMapper histFundInfoMapper;

    @Override
    public List<HistFundInfo> findHistFundInfo(String code, SearchBean searchBean) {
        PageHelper.startPage(searchBean.getCurrentPage(),searchBean.getPageSize());

        HistFundInfo queryParams = new HistFundInfo();
        queryParams.setCode(code);
        List<HistFundInfo> histFundInfoList = histFundInfoMapper.findHistFundInfo(code);
        return histFundInfoList;
    }
}
