package xyz.seiyaya.common.bean;

import lombok.Data;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/14 10:11
 */
@Data
public class LockBean {

    private String key;

    private String value;

    public LockBean(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
