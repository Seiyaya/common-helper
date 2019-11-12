package xyz.seiyaya.fund.service;

import xyz.seiyaya.common.bean.SearchBean;
import xyz.seiyaya.fund.bean.HistFundInfo;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 15:52
 */
public interface FundInfoService {
    /**
     * 查询历史基金净值信息
     * @param code
     * @param searchBean
     * @return
     */
    List<HistFundInfo> findHistFundInfo(String code, SearchBean searchBean);
}
