package xyz.seiyaya.common.mybatis.helper;

import xyz.seiyaya.common.helper.CollectionHelper;
import xyz.seiyaya.common.mybatis.base.BaseMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来做批处理，批量插入和批量更新
 * @author wangjia
 * @version v1.0
 * @date 2020/12/21 16:39
 */
public class BatchHelper {

    private static final int DEFAULT_SIZE = 200;

    /**
     * 默认直接用200切割
     * @param baseMapper
     * @param list
     * @param <T>
     */
    public static <T> void batchInsert(BaseMapper<T> baseMapper,List<T> list){
        List<T> insertList = new ArrayList<>();
        for(T t : list){
            insertList.add(t);
            if(insertList.size() >= DEFAULT_SIZE){
                baseMapper.batchInsert(insertList);
                insertList.clear();
            }
        }
        if(CollectionHelper.isNotEmpty(insertList)){
            baseMapper.batchInsert(insertList);
        }
    }
}
