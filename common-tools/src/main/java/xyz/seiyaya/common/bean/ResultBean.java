package xyz.seiyaya.common.bean;

import lombok.Data;
import xyz.seiyaya.common.constant.ResultConstant;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author seiyaya
 * @version 1.0
 * @date: 2019/9/6 14:43
 */
@Data
public class ResultBean implements Serializable {
    private String msg = ResultConstant.CODE_SUCCESS.getMsg();

    public static final String DEFAULT_KEY = "data";
    /**
     *
     */
    private int code = ResultConstant.CODE_SUCCESS.getCode();
    private HashMap<String, Object> results = new HashMap<>();

    public ResultBean() {
    }

    public ResultBean(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public ResultBean(String msg) {
        this.msg = msg;
    }

    public ResultBean setResults(String key , Object value) {
        this.results.put(key, value);
        return this;
    }

    public ResultBean setData(Object value){
        results.put(DEFAULT_KEY,value);
        return this;
    }

    /**
     * 设置返回code和msg
     * @param resultConstant
     * @return
     */
    public ResultBean setCodeAndMsg(ResultConstant resultConstant){
        this.code = resultConstant.getCode();
        this.msg = resultConstant.getMsg();
        return this;
    }

    /**
     * 设置错误信息，因为比较常用，所以单独提出来
     * @return
     */
    public ResultBean setError(){
        this.code = ResultConstant.CODE_ERROR.getCode();
        this.msg = ResultConstant.CODE_ERROR.getMsg();
        return this;
    }

    /**
     * 设置错误信息，因为比较常用，所以单独提出来
     * @return
     */
    public ResultBean setError(String msg){
        this.code = ResultConstant.CODE_ERROR.getCode();
        this.msg = msg;
        return this;
    }

    /**
     * 设置参数异常的返回结果集
     * @return
     */
    public ResultBean setParamError(){
        return this.setError(ResultConstant.CODE_PARAM_ERROR.getMsg());
    }

    /**
     * 设置参数异常的返回结果集
     * @param msg
     * @return
     */
    public ResultBean setParamError(String msg){
        this.code = ResultConstant.CODE_PARAM_ERROR.getCode();
        this.msg = msg;
        return this;
    }
}
