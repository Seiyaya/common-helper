package xyz.seiyaya.stock.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.seiyaya.stock.bean.StockInfo;

/**
 * @author seiyaya
 * @date 2020/1/21 10:48
 */
@Mapper
public interface StockInfoMapper extends tk.mybatis.mapper.common.Mapper<StockInfo> {
}
