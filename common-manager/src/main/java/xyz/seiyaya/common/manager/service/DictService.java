package xyz.seiyaya.common.manager.service;

import xyz.seiyaya.common.manager.bean.dict.dto.DictDTO;
import xyz.seiyaya.common.manager.bean.dict.model.Dict;
import xyz.seiyaya.common.manager.bean.dict.vo.DictVO;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/4/2 10:28
 */
public interface DictService {

    /**
     * 查询数据字典信息
     * @param dictNo
     * @return
     */
    Dict queryDictByDictNo(String dictNo);

    /**
     * 查询动态数据字典
     * @param dictId
     * @param dictDTO
     * @return
     */
    DictVO queryDynamicDict(String dictId, DictDTO dictDTO);

    /**
     * 查询静态数据字典
     * @param dictId
     * @return
     */
    DictVO queryStaticDict(String dictId);
}
