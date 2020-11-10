package xyz.seiyaya.common.bean;

import lombok.Data;
import xyz.seiyaya.common.annotation.CrawlAttribute;
import xyz.seiyaya.common.annotation.UpdateLogInfo;
import xyz.seiyaya.common.helper.DateHelper;

import java.util.Date;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/16 11:40
 */
@Data
public class User {

    @UpdateLogInfo("用户id")
    private String userId;

    @CrawlAttribute("fengshen")
    @UpdateLogInfo("用户名")
    private String username;
    private Address address;

    @UpdateLogInfo(value = "生日",datePattern = DateHelper.YYYY_MM_DD_HH_MM_SS)
    private Date birthday;

    @UpdateLogInfo(value = "地址")
    private List<Address> addressList;

    private Integer status;
}
