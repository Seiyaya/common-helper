package xyz.seiyaya.common.constant;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/16 10:49
 */
public enum ResultConstant {
    /**
     * 成功
     */
    CODE_SUCCESS(200, "成功"),

    /**
     * 参数不符合规范
     */
    CODE_PARAM_ERROR(151,"参数错误"),

    /**
     * 失败
     */
    CODE_ERROR(500, "系统错误"),

    /**
     * 重复提交
     */
    CODE_REPEAT(150, "当前操作重复执行，请稍后再试");

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

}
