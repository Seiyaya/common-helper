package xyz.seiyaya.mybatis.generate.quartz.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 图片上传实体
 * 
 * @author seiyaya
 * @version 1.0.0
 * @date 2019-10-08
 */
@Data
public class UploadPic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键Id
    */
    private Long id;

    /**
    * 上传的文件类型
    */
    private String type;

    /**
    * 图片存储的地址
    */
    private String url;

    /**
    * 创建时间
    */
    private Date createDate;

}