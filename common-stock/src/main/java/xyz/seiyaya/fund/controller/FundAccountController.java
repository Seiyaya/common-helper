package xyz.seiyaya.fund.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.bean.SearchBean;
import xyz.seiyaya.fund.bean.FundAccount;
import xyz.seiyaya.fund.bean.FundBargain;
import xyz.seiyaya.fund.bean.vo.FundHoldVo;
import xyz.seiyaya.fund.service.AccountService;
import xyz.seiyaya.fund.service.BargainService;
import xyz.seiyaya.fund.service.FundHoldService;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 11:23
 */
@RestController
@RequestMapping("/account")
public class FundAccountController extends BaseController {

    @Autowired
    private FundHoldService fundHoldService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BargainService bargainService;

    @PostMapping("/hold/{accountId}")
    public ResultBean getHold(@PathVariable("accountId")Integer accountId){
        ResultBean resultBean = new ResultBean();
        List<FundHoldVo> list = fundHoldService.findHoldWithQuotes(accountId);
        resultBean.setData(list);
        return resultBean;
    }

    @PostMapping("/info/{accountId}")
    public ResultBean getAccountInfo(@PathVariable("accountId") Integer accountId){
        ResultBean resultBean = new ResultBean();
        FundAccount fundAccount = accountService.findAccount(accountId);
        resultBean.setData(fundAccount);
        return resultBean;
    }

    @PostMapping("/bargain/{accountId}")
    public ResultBean getBargain(@RequestBody SearchBean searchBean, @PathVariable("accountId") Integer accountId){
        ResultBean resultBean = new ResultBean();
        searchBean.validate();

        List<FundBargain> bargainList = bargainService.findBargain(accountId,searchBean);
        resultBean.setData(bargainList);
        return resultBean;
    }
}
