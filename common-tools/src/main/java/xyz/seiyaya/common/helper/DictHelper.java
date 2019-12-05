package xyz.seiyaya.common.helper;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import xyz.seiyaya.common.bean.Dict;
import xyz.seiyaya.common.bean.RedisConstantBean;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据字典工具类，主要是用来给出参进行参数替换
 * @author wangjia
 * @version 1.0
 * @date 2019/11/27 11:22
 */
@Component
public class DictHelper {

    @Resource
    private RedisTemplate<String, Map<Object,String>> redisTemplate;

    private static final String DEFAULT_VALUE = "--";

    /**
     * 获取字典值
     * @param dictType
     * @param key
     */
    @SuppressWarnings("all")
    public String getDictValue(String dictType,String key){
        String result = redisTemplate.opsForHash().get(RedisConstantBean.DICT_MAP, dictType).toString();
        if(StringHelper.isNotEmpty(result)){
            Map dictMap = JSONObject.parseObject(result, Map.class);
            return dictMap.getOrDefault(key,DEFAULT_VALUE).toString();
        }else{
            return DEFAULT_VALUE;
        }
    }

    /**
     * 添加字典值到redis
     * @param dictList
     */
    public void putAllDict(List<Dict> dictList){
        Map<String,Map<Object,String>> dictMap = new HashMap<>();
        dictList.forEach(model->{
            Map<Object, String> maps = dictMap.get(model.getParentType());
            if(maps == null){
                maps = new HashMap<>();
                maps.put(model.getDictKey(),model.getDictValue());
                dictMap.put(model.getParentType(),maps);
            }else{
                maps.put(model.getDictKey(),model.getDictValue());
            }
        });
        redisTemplate.opsForHash().putAll(RedisConstantBean.DICT_MAP,dictMap);
    }
}
