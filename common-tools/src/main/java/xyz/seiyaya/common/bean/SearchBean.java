package xyz.seiyaya.common.bean;

import lombok.Data;
import xyz.seiyaya.common.helper.DateHelper;

import java.util.Date;

import static xyz.seiyaya.common.helper.ObjectHelper.ifNull;

/**
 * 通用查询的bean
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 14:03
 */
@Data
public class SearchBean {
    private Date startDate;
    private Date endDate;
    private Integer currentPage;
    private Integer pageSize;
    
    public void validate(){
        validateDate();
        validatePage();
    }

    public void validatePage(){
        ifNull(currentPage,1);
        ifNull(pageSize,10);
    }

    public void validateDate(){
        ifNull(startDate, DateHelper.getNDate(-30));
        ifNull(endDate, new Date());
    }
}
