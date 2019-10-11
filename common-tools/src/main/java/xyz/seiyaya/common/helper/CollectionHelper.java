package xyz.seiyaya.common.helper;

import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/9 16:25
 */
public class CollectionHelper extends CollectionUtils {

    /**
     * 判断是否不为空
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }
}
