package xyz.seiyaya.fund.bean;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/7 9:44
 */
@Data
@Table(name = "t_fund_follow")
public class FundFollow {

    private Integer id;
    private String code;
    /**
     * 0有效1无效
     */
    private Integer status;

    private Date createDate;
}
