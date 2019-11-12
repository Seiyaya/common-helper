package xyz.seiyaya.fund.mapper;

import tk.mybatis.mapper.common.Mapper;
import xyz.seiyaya.fund.bean.FundBargain;
import xyz.seiyaya.fund.bean.dto.FundBargainDto;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/7 9:12
 */
@org.apache.ibatis.annotations.Mapper
public interface FundBargainMapper extends Mapper<FundBargain> {

    List<FundBargain> findFundBargain(FundBargainDto fundBargainDto);
}
