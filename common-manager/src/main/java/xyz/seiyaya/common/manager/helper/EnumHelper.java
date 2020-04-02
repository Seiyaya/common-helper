package xyz.seiyaya.common.manager.helper;

import xyz.seiyaya.common.cache.service.CacheService;
import xyz.seiyaya.common.helper.SpringHelper;
import xyz.seiyaya.common.manager.bean.constant.DictConstant;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/4/2 10:17
 */
public class EnumHelper {

    private static CacheService cacheService = SpringHelper.getBean(CacheService.CACHE_SERVICE_REDIS, CacheService.class);

    /**
     * 获取list的json数据
     * @param dictNo
     * @return
     */
    public static String getListString(String dictNo) {
        return cacheService.hget(DictConstant.DICT_ENUM_KEY, dictNo);
    }
}
