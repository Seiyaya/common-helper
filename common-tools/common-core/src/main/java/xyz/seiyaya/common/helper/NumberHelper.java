package xyz.seiyaya.common.helper;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author wangjia
 * @version v1.0
 * @date 2020/12/12 15:05
 */
@Slf4j
public class NumberHelper {

    public static BigDecimal parseBigDecimal(String str){
        if(StringHelper.isEmpty(str)){
            return BigDecimal.ZERO;
        }

        try {
            return new BigDecimal(str);
        }catch (Exception ignored){
        }
        return BigDecimal.ZERO;
    }

    /**
     * 解析byte
     * @param str
     * @return
     */
    public static Byte parseByte(String str) {
        try {
            return Byte.valueOf(str);
        }catch (Exception ignored){
        }
        return Byte.MIN_VALUE;
    }
}
