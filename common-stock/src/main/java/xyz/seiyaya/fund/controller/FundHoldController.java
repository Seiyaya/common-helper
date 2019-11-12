package xyz.seiyaya.fund.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.fund.bean.vo.FundHoldVo;
import xyz.seiyaya.fund.service.FundHoldService;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 16:18
 */
@RestController
@RequestMapping("/hold")
public class FundHoldController extends BaseController {

    @Autowired
    private FundHoldService fundHoldService;

    @PostMapping("/{accountId}")
    public ResultBean getHold(@PathVariable("accountId")Integer accountId){
        ResultBean resultBean = new ResultBean();
        List<FundHoldVo> list = fundHoldService.findHoldWithQuotes(accountId);
        resultBean.setData(list);
        return resultBean;
    }
}
