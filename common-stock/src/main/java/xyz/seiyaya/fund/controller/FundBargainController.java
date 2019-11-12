package xyz.seiyaya.fund.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.bean.SearchBean;
import xyz.seiyaya.fund.bean.FundBargain;
import xyz.seiyaya.fund.service.FundBargainService;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 16:19
 */
@RestController
@RequestMapping("/bargain")
public class FundBargainController extends BaseController {

    @Autowired
    private FundBargainService fundBargainService;

    @PostMapping("/{accountId}")
    public ResultBean getBargain(@RequestBody SearchBean searchBean, @PathVariable("accountId") Integer accountId){
        ResultBean resultBean = new ResultBean();
        searchBean.validate();

        List<FundBargain> bargainList = fundBargainService.findBargain(accountId,searchBean);
        resultBean.setData(bargainList);
        return resultBean;
    }

    /**
     * 录入成交
     * @param fundBargain
     * @return
     */
    @PostMapping("/add")
    public ResultBean addBargain(@RequestBody FundBargain fundBargain){
        ResultBean resultBean = new ResultBean();
        fundBargainService.addBargain(fundBargain);
        return resultBean;
    }
}
