package xyz.seiyaya.fund.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.bean.SearchBean;
import xyz.seiyaya.fund.bean.FundAccount;
import xyz.seiyaya.fund.bean.FundBargain;
import xyz.seiyaya.fund.bean.FundFollow;
import xyz.seiyaya.fund.bean.vo.FundHoldVo;
import xyz.seiyaya.fund.service.FundAccountService;
import xyz.seiyaya.fund.service.FundBargainService;
import xyz.seiyaya.fund.service.FundFollowService;
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
    private FundAccountService fundAccountService;

    @PostMapping("/info/{accountId}")
    public ResultBean getAccountInfo(@PathVariable("accountId") Integer accountId){
        ResultBean resultBean = new ResultBean();
        FundAccount fundAccount = fundAccountService.findAccount(accountId);
        resultBean.setData(fundAccount);
        return resultBean;
    }
}
