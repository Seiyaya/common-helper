package xyz.seiyaya.fund.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.seiyaya.fund.bean.FundInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 11:01
 */
@Component
public class QuotesCacheHelper {

    @Autowired
    private QuotesHelper quotesHelper;

    private List<FundInfo> fundInfoList = new ArrayList<>();

    private Map<String,FundInfo> fundInfoMap = new ConcurrentHashMap<>();

    private long lastTime = 0;

    public List<FundInfo> getFundInfoList(List<String> codeList){
        long result = (System.currentTimeMillis() - lastTime) / 1000;
        if(lastTime == 0 || result > 3){
            initQuotesInfo(codeList);
        }
        return this.fundInfoList;
    }

    public Map<String, FundInfo> getFundInfoMap(List<String> codeList) {
        long result = (System.currentTimeMillis() - lastTime) / 1000;
        if(lastTime == 0 || result > 3){
            initQuotesInfo(codeList);
        }
        return this.fundInfoMap;
    }

    /**
     * 初始化行情信息
     * @param codeList
     */
    private void initQuotesInfo(List<String> codeList) {
        List<FundInfo> fundInfoList = quotesHelper.getFundInfoList(codeList);
        this.fundInfoList = fundInfoList;
        Map<String,FundInfo> tmpMap = new ConcurrentHashMap<>();
        fundInfoList.forEach(model->{
            tmpMap.put(model.getCode(),model);
        });
        fundInfoMap = tmpMap;
    }


}
