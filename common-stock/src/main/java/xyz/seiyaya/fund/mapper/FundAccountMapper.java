package xyz.seiyaya.fund.mapper;

import tk.mybatis.mapper.common.Mapper;
import xyz.seiyaya.fund.bean.FundAccount;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/7 9:12
 */
@org.apache.ibatis.annotations.Mapper
public interface FundAccountMapper extends Mapper<FundAccount> {

    FundAccount findFundAccount(Integer id);
}
