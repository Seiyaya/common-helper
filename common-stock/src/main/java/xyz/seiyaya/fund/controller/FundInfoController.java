package xyz.seiyaya.fund.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.bean.SearchBean;
import xyz.seiyaya.fund.bean.FundInfo;
import xyz.seiyaya.fund.bean.HistFundInfo;
import xyz.seiyaya.fund.helper.QuotesCacheHelper;
import xyz.seiyaya.fund.service.FundInfoService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 行情信息
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 11:46
 */
@RestController
@RequestMapping("/fund")
public class FundInfoController extends BaseController {

    @Autowired
    private QuotesCacheHelper quotesCacheHelper;

    @Resource
    private FundInfoService fundInfoService;

    @PostMapping("/info/{code}")
    public ResultBean getFundInfo(@PathVariable String code){
        ResultBean resultBean = new ResultBean();
        FundInfo fundInfo = quotesCacheHelper.getFundCodeInfo(code);

        resultBean.setData(fundInfo);
        return resultBean;
    }

    @PostMapping("/hist/info/{code}")
    public ResultBean getHistFundInfo(@PathVariable String code, @RequestBody SearchBean searchBean){
        ResultBean resultBean = new ResultBean();
        searchBean.validatePage();

        List<HistFundInfo> histFundInfoList = fundInfoService.findHistFundInfo(code,searchBean);

        resultBean.setData(histFundInfoList);
        return resultBean;
    }
}
