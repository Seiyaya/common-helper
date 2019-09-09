package xyz.seiyaya.common.helper;

import org.apache.commons.lang3.StringUtils;

/**
 * @author seiyaya
 * @date 2019/8/13 15:26
 */
public class StringHelper extends StringUtils {

    /**
     * 下划线方式的命名转成驼峰
     * @param str
     * @return
     */
    public static String splitCharTransToUpper(String str){
        StringBuilder result = new StringBuilder();

        String[] splits = StringUtils.split(str, "_");
        for(String split:splits){
            result.append(StringUtils.capitalize(split));
        }

        return result.toString();
    }
}
