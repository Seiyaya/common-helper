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
    public static <T> void batchInsert(BaseMapper<T> baseMapper,List<T> list,Boolean isLast){
        if(isLast){
            if(CollectionHelper.isNotEmpty(list)){
                baseMapper.batchInsert(list);
                list.clear();
            }
        }else{
            if(list.size() >= DEFAULT_SIZE){
                baseMapper.batchInsert(list);
            }
        }
    }


    /**
     * 批量插入，默认不是插入所有
     * @param baseMapper
     * @param list
     * @param <T>
     */
    public static <T> void batchInsert(BaseMapper<T> baseMapper,List<T> list){
        batchInsert(baseMapper,list,Boolean.FALSE);
    }


    /**
     * 批量插入，默认不是插入所有
     * @param baseMapper
     * @param list
     * @param <T>
     */
    public static <T> void insertList(BaseMapper<T> baseMapper,List<T> list){
        insertList(baseMapper,list,Boolean.FALSE);
    }


    /**
     * 默认直接用200切割
     * @param baseMapper
     * @param list
     * @param <T>
     */
    public static <T> void insertList(BaseMapper<T> baseMapper,List<T> list,Boolean isLast){
        if(isLast){
            if(CollectionHelper.isNotEmpty(list)){
                baseMapper.insertList(list);
                list.clear();
            }
        }else{
            if(list.size() >= DEFAULT_SIZE){
                baseMapper.insertList(list);
            }
        }
    }
}
