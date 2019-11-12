package xyz.seiyaya.fund.bean;

import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 历史净值信息
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 11:22
 */
@Data
@Table(name = "t_hist_fund_info")
public class HistFundInfo extends FundInfo{

    private Integer backupDate;

    public HistFundInfo(){

    }

    public HistFundInfo(Integer backupDate,BigDecimal price,BigDecimal sumPrice){
        this.backupDate = backupDate;
        setPrice(price);
        setSumPrice(sumPrice);
    }
}
