package xyz.seiyaya.stock.service;

/**
 * @author seiyaya
 * @date 2020/1/21 10:48
 */
public interface StockInfoService {

    /**
     * 添加历史行情信息
     * @param marketId
     * @param stockCode
     */
    void insertHistStockInfo(String marketId,String stockCode);

    /**
     * 添加历史行情信息
     * 近 count 个交易日
     * @param marketId
     * @param stockCode
     * @param count
     */
    void insertHistStockInfo(String marketId,String stockCode,int count);
}
