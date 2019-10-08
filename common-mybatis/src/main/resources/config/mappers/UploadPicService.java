package xyz.seiyaya.mybatis.generate.quartz.service;

import xyz.seiyaya.common.base.BaseService;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * 图片上传Service
 *
 * @author seiyaya
 * @version 1.0.0
 * @date 2019-10-08
 */
public interface UploadPicService extends BaseService<UploadPic, Long>{


    /**
     * 更新
     * @param params
     * @return int
     * */
    int updateByMap(Map<String, Object> params);

    /**
     * 查询集合
     * @param params
     * @return  List<UploadPic>
    * */
    List<UploadPic> select(Map<String, Object> params);

    /**
    * 分页查询
    * @param current
    * @param pageSize
    * @param params
    * @return  Page<UploadPic>
    * */
    Page<UploadPic> page(Integer current, Integer pageSize , Map<String, Object> params);

    /**
    * 单数查询
    * @param params
    * @return  UploadPic
    * */
    UploadPic find(Map<String, Object> params);

    /**
    * 根据主键查询
    * @param id
    * @return  UploadPic
    * */
    UploadPic findByPrimary(Long id);
}
