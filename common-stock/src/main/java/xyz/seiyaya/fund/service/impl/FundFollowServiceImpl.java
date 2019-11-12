package xyz.seiyaya.fund.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.fund.bean.FundFollow;
import xyz.seiyaya.fund.mapper.FundFollowMapper;
import xyz.seiyaya.fund.service.FundFollowService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 15:42
 */
@Service
@Transactional
public class FundFollowServiceImpl implements FundFollowService {

    @Resource
    private FundFollowMapper fundFollowMapper;

    @Override
    public List<FundFollow> findFollow(Integer accountId) {
        FundFollow fundFollow = new FundFollow();
        fundFollow.setAccountId(accountId);
        List<FundFollow> fundFollowList = fundFollowMapper.select(fundFollow);
        return fundFollowList;
    }
}
