package xyz.seiyaya.common.manager.bean.dict.vo;

import lombok.Data;
import xyz.seiyaya.common.manager.bean.dict.model.Dict;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/4/2 10:19
 */
@Data
public class DictVO extends Dict {

    private String dictInfo;


    public DictVO(String dictInfo ,String dictNo) {
        this.dictInfo = dictInfo;
        this.setDictNo(dictNo);
    }
}
