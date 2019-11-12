package xyz.seiyaya.fund.service;

import xyz.seiyaya.fund.bean.FundFollow;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 15:42
 */
public interface FundFollowService {

    /**
     * 查询关注列表
     * @param accountId
     * @return
     */
    List<FundFollow> findFollow(Integer accountId);
}
