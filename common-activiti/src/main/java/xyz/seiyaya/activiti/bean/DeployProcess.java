package xyz.seiyaya.activiti.bean;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/2 17:42
 */
@Data
public class DeployProcess {

    /**
     * xml文件
     */
    private MultipartFile xmlResource;

    /**
     * 图片文件
     */
    private MultipartFile pngResource;

    /**
     * 流程图的名称
     */
    private String name;


    public DeployProcess() {
    }

    public DeployProcess(MultipartFile xmlResource, MultipartFile pngResource, String name) {
        this.xmlResource = xmlResource;
        this.pngResource = pngResource;
        this.name = name;
    }
}
