package xyz.seiyaya.fund.service;

import xyz.seiyaya.fund.bean.FundHold;
import xyz.seiyaya.fund.bean.vo.FundHoldVo;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 10:39
 */
public interface FundHoldService {

    /**
     * 查询持仓附带行情信息
     * @param accountId
     * @return
     */
    List<FundHoldVo> findHoldWithQuotes(Integer accountId);

    /**
     * 单独只查询持仓
     * @param accountId
     * @return
     */
    List<FundHold> findHold(Integer accountId);
}
