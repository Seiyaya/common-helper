package xyz.seiyaya.common.cache.helper;

import xyz.seiyaya.common.constant.ResultConstant;
import xyz.seiyaya.common.exception.ParamsException;

/**
 * 校验工具
 * @author wangjia
 * @version 1.0
 * @date 2020/8/10 16:50
 */
public class CheckConditionHelper {

    public static void checkParams(boolean condition,String msg){
        check(condition, ResultConstant.CODE_PARAM_ERROR.getCode(),msg);
    }

    public static void check(boolean condition,ResultConstant result){
        check(condition, result.getCode(),result.getMsg());
    }

    public static void check(boolean condition,int key,String msg){
        if(!condition){
            throw new ParamsException(key,msg);
        }
    }
}
