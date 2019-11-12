package xyz.seiyaya.fund.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.seiyaya.fund.bean.FundFollow;
import xyz.seiyaya.fund.bean.FundInfo;
import xyz.seiyaya.fund.mapper.FundFollowMapper;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 11:01
 */
@Component
public class QuotesCacheHelper {

    @Autowired
    private QuotesHelper quotesHelper;

    @Resource
    private FundFollowMapper fundFollowMapper;

    private List<FundInfo> fundInfoList = new ArrayList<>();

    private Map<String,FundInfo> fundInfoMap = new ConcurrentHashMap<>();

    private Set<String> codeList = ConcurrentHashMap.newKeySet();

    private long lastTime = 0;

    /**
     * 获取需要查询的列表
     * 后面把这个重构成redis队列来进行添加关注基金之后加入需要查询行情的模式
     * @return
     */
    public List<String> getCodeList(){
        return new ArrayList<>(codeList);
    }

    /**
     * 重置需要查询的列表
     */
    public void resetCodeList(){
        codeList = fundFollowMapper.selectAll().stream().map(FundFollow::getCode).collect(Collectors.toSet());
    }


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


    /**
     * 获取金信息
     * @param code
     * @return
     */
    public FundInfo getFundCodeInfo(String code) {
        return fundInfoMap.get(code);
    }
}
