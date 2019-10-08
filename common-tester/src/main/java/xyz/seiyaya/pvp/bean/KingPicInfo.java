package xyz.seiyaya.pvp.bean;

import lombok.Data;
import xyz.seiyaya.common.helper.URLHelper;

import java.util.List;

/**
 * @author seiyaya
 * @date 2019/10/3 18:59
 */
@Data
public class KingPicInfo {

    private Integer id;
    private String inputTime;
    private String productName;
    private List<KingPicInfo> kingPicInfoList;

    public void setInputTime(String inputTime) {
        this.inputTime = URLHelper.urlDecoder(inputTime);
    }

    public void setProductName(String productName) {
        this.productName = URLHelper.urlDecoder(productName);
    }
}
