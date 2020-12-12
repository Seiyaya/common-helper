package xyz.seiyaya.common.mybatis.tk;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 用来解决tkmybatis只能批量插入自增主键的数据
 * @author wangjia
 * @version v1.0
 * @date 2020/12/12 11:03
 */
@RegisterMapper
public interface BatchMapper<T> {

    /**
     * 批量插入
     * @param recordList
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = BatchProvider.class, method = "batchInsert")
    int batchInsert(List<T> recordList);
}
