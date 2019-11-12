package xyz.seiyaya.fund.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.fund.bean.FundFollow;
import xyz.seiyaya.fund.service.FundFollowService;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 16:20
 */
@RestController
@RequestMapping("/follow")
public class FundFollowController extends BaseController {

    @Autowired
    private FundFollowService fundFollowService;

    @PostMapping("/{accountId}")
    public ResultBean getFollow(@PathVariable("accountId") Integer accountId){
        ResultBean resultBean = new ResultBean();
        List<FundFollow> fundFollowList = fundFollowService.findFollow(accountId);
        resultBean.setData(fundFollowList);
        return resultBean;
    }
}
