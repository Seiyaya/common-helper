package xyz.seiyaya.fund.bean.dto;

import lombok.Data;
import xyz.seiyaya.fund.bean.FundBargain;

import java.util.Date;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 14:15
 */
@Data
public class FundBargainDto extends FundBargain {

    private Date startDate;
    private Date endDate;
}
