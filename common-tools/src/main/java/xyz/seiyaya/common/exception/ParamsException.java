package xyz.seiyaya.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用来进行参数校验
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 18:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ParamsException extends RuntimeException {


    private int key;

    private String msg;

    public ParamsException() {
    }

    public ParamsException(int key,String message) {
        super(message);
        this.key = key;
        this.msg = message;
    }
}
