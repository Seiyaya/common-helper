package xyz.seiyaya.common.bean;

import lombok.Data;

import java.util.Date;

/**
 * 记录网络请求日志
 * @author wangjia
 * @version 1.0
 * @date 2019/10/25 11:41
 */
@Data
public class RequestLog {

    private Integer id;
    /**
     * 请求的类型
     */
    private Integer type;
    /**
     * 请求内容
     */
    private String requestUrl;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 响应内容
     */
    private String responseContent;
    /**
     * 请求消耗时间
     */
    private Integer costTime;
    /**
     * 创建日期
     */
    private Date createDate;
}
