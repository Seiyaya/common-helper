package xyz.seiyaya.common.exception;

import lombok.Data;
import xyz.seiyaya.common.constant.ResultConstant;

/**
 * 用来进行参数校验
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 18:30
 */
@Data
public class ParamsException extends RuntimeException {


    private ResultConstant result;

    public ParamsException() {
    }

    public ParamsException(String message) {
        super(message);
        this.result = ResultConstant.CODE_PARAM_ERROR;
    }
}
