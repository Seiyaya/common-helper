package xyz.seiyaya.common.manager.bean.dict.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/4/2 10:03
 */
@Data
@Table(name = "t_dict_type")
public class Dict {

    @Id
    private String dictId;
    private String dictNo;
    private String dictName;
    /**
     * 是否是动态数据字典
     */
    private Byte isDynamic;
    /**
     * 具体的字典项
     */
    private List<DictItem> enumList;
    /**
     * 是否缓存
     */
    private Byte isCache;

    public Dict(){

    }

    @Builder
    public Dict(String dictId, String dictNo, String dictName, Byte isDynamic, List<DictItem> enumList, Byte isCache) {
        this.dictId = dictId;
        this.dictNo = dictNo;
        this.dictName = dictName;
        this.isDynamic = isDynamic;
        this.enumList = enumList;
        this.isCache = isCache;
    }
}
