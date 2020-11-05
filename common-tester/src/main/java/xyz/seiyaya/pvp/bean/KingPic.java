package xyz.seiyaya.pvp.bean;

import lombok.Data;
import xyz.seiyaya.common.cache.helper.URLHelper;

/**
 * 具体的图片
 * @author seiyaya
 * @date 2019/10/3 19:04
 */
@Data
public class KingPic {

    private Integer id;
    private Integer infoId;
    private String url;

    public KingPic(Integer infoId,String url){
        this.infoId = infoId;
        this.url = URLHelper.urlDecoder(url);
    }
}
