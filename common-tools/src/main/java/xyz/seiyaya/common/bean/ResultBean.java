package xyz.seiyaya.common.bean;

import lombok.Data;

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

    public void setData(Object value){
        results.put(DEFAULT_KEY,value);
    }

    /**
     * code常量
     */
    enum ResultConstant{

        /**
         * 成功
         */
        CODE_SUCCESS(200, "成功"),

        /**
         * 失败
         */
        CODE_ERROR(500, "失败");

        private int code;
        private String msg;

        ResultConstant(int key,String value){
            this.code = key;
            this.msg = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public static ResultConstant getValueByKey(int key) {
            for (ResultConstant errorType : ResultConstant.values()) {
                if (key == errorType.getCode()) {
                    return errorType;
                }
            }
            return null;
        }
    }
}
