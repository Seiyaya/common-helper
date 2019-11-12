package xyz.seiyaya.fund.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.seiyaya.fund.bean.FundAccount;
import xyz.seiyaya.fund.mapper.FundAccountMapper;
import xyz.seiyaya.fund.service.AccountService;

import javax.annotation.Resource;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 13:53
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private FundAccountMapper fundAccountMapper;

    @Override
    public FundAccount findAccount(Integer accountId) {
        return fundAccountMapper.selectByPrimaryKey(accountId);
    }
}
