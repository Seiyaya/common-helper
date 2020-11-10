package xyz.seiyaya.stock.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.helper.StringHelper;
import xyz.seiyaya.stock.bean.Stock;
import xyz.seiyaya.stock.bean.dto.StockDto;
import xyz.seiyaya.stock.service.StockService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/18 16:36
 */
@RestController
@RequestMapping("stock")
public class StockController {

    @Resource
    private StockService stockService;

    @RequestMapping("/draw")
    public ResultBean draw(@RequestBody StockDto stockDto){
        if(stockDto.getStartDate() == null || stockDto.getEndDate() == null){
            return new ResultBean().setError("绘图的条件不存在");
        }
        if(stockDto.getStartDate() > stockDto.getEndDate()
                || StringHelper.length(stockDto.getStartDate().toString()) != 8
                ||  StringHelper.length(stockDto.getEndDate().toString()) != 8 ){
            return new ResultBean().setError("绘图的条件逻辑错误");
        }
        ResultBean resultBean = new ResultBean();
        List<Stock> list = stockService.findByCondition(stockDto);
        resultBean.setData(list);
        return resultBean;
    }
}
