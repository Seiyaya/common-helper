package xyz.seiyaya.fund.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.fund.bean.FundAccount;
import xyz.seiyaya.fund.mapper.FundAccountMapper;
import xyz.seiyaya.fund.service.FundAccountService;

import javax.annotation.Resource;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 13:53
 */
@Service
@Transactional
public class FundAccountServiceImpl implements FundAccountService {

    @Resource
    private FundAccountMapper fundAccountMapper;

    @Override
    public FundAccount findAccount(Integer accountId) {
        return fundAccountMapper.selectByPrimaryKey(accountId);
    }
}
