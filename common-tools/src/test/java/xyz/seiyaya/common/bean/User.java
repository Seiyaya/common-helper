package xyz.seiyaya.common.bean;

import lombok.Data;
import xyz.seiyaya.common.annotation.CrawlAttribute;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/16 11:40
 */
@Data
public class User {

    private String userId;

    @CrawlAttribute("username")
    private String username;
    private Address address;
}
