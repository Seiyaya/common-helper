package xyz.seiyaya.fund.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import xyz.seiyaya.fund.bean.HistFundInfo;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/7 9:12
 */
@org.apache.ibatis.annotations.Mapper
public interface HistFundInfoMapper extends Mapper<HistFundInfo> {

    /**
     * 历史净值查询
     * @param code
     * @return
     */
    List<HistFundInfo> findHistFundInfo(@Param("code") String code);
}
