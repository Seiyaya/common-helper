package xyz.seiyaya.stock.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.base.BaseController;

/**
 * 交易: 包含持仓、成交、委托等信息
 * @author seiyaya
 * @date 2020/1/22 17:52
 */
@RestController
@RequestMapping("/trade")
public class StockTradeController extends BaseController {
}
