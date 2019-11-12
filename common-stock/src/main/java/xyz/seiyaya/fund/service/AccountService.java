package xyz.seiyaya.fund.service;

import xyz.seiyaya.fund.bean.FundAccount;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 13:53
 */
public interface AccountService {
    /**
     * 查询账户信息
     * @param accountId
     * @return
     */
    FundAccount findAccount(Integer accountId);
}
