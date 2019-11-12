package xyz.seiyaya.fund.service;

import xyz.seiyaya.common.bean.SearchBean;
import xyz.seiyaya.fund.bean.FundBargain;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 14:00
 */
public interface FundBargainService {

    /**
     * 查找成交记录
     * @param accountId
     * @param searchBean
     * @return
     */
    List<FundBargain> findBargain(Integer accountId, SearchBean searchBean);

    /**
     * 录入成交
     * @param fundBargain
     */
    void addBargain(FundBargain fundBargain);
}
