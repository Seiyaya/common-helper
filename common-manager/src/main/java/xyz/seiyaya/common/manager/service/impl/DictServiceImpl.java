package xyz.seiyaya.common.manager.service.impl;

import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import xyz.seiyaya.common.config.Constant;
import xyz.seiyaya.common.manager.bean.dict.dto.DictDTO;
import xyz.seiyaya.common.manager.bean.dict.model.Dict;
import xyz.seiyaya.common.manager.bean.dict.model.DictItem;
import xyz.seiyaya.common.manager.bean.dict.vo.DictVO;
import xyz.seiyaya.common.manager.mapper.DictItemMapper;
import xyz.seiyaya.common.manager.mapper.DictMapper;
import xyz.seiyaya.common.manager.service.DictService;

import javax.annotation.Resource;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/4/2 10:29
 */
@Service
public class DictServiceImpl implements DictService {

    @Resource
    private DictMapper dictMapper;

    @Resource
    private DictItemMapper dictItemMapper;

    @Override
    public Dict queryDictByDictNo(String dictNo) {
        Dict dict = dictMapper.selectOne(Dict.builder().dictNo(dictNo).build());
        return dict;
    }

    @Override
    public DictVO queryDynamicDict(String dictId, DictDTO dictDTO) {
        return null;
    }

    @Override
    public DictVO queryStaticDict(String dictId) {
        Example example = new Example(DictItem.class);
        example.createCriteria().andEqualTo("dictId", dictId).andEqualTo("status", Constant.ByteConstant.BYTE_1);
        example.orderBy("sort").desc();
        dictItemMapper.selectByExample(example);
        return null;
    }
}
