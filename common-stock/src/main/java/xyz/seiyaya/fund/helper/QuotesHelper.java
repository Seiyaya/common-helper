package xyz.seiyaya.fund.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.seiyaya.common.helper.DBParam;
import xyz.seiyaya.common.helper.DateHelper;
import xyz.seiyaya.common.helper.HttpHelper;
import xyz.seiyaya.common.helper.StringHelper;
import xyz.seiyaya.fund.bean.FundInfo;
import xyz.seiyaya.fund.bean.HistFundInfo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 13:49
 */
@Component
@Slf4j
public class QuotesHelper {

    private HttpHelper instance;

    private static Pattern FUND_INFO_PATTERN = Pattern.compile("_(\\d*)=\"(.*?)\"");

    private static String NEW_LINE = "\n";

    public QuotesHelper(){
        instance = HttpHelper.getHttpUtils();
    }

    /**
     * 获取行情列表
     * @param codeList
     * @return
     */
    public List<FundInfo> getFundInfoList(List<String> codeList){
        Map<String, FundInfo> result = getFundInfoMap(codeList);
        List<FundInfo> fundInfos = Lists.newArrayList();
        result.forEach((k,v)->{
            if(StringHelper.isNotEmpty(v.getName())){
                fundInfos.add(v);
            }
        });
        return fundInfos;
    }

    /**
     * 获取行情map，key是code,value是行情信息
     * @param codeList
     * @return
     */
    public Map<String,FundInfo> getFundInfoMap(List<String> codeList){
        StringBuilder codeParam = new StringBuilder();
        Map<String,FundInfo> result = new HashMap<>();
        for(String code : codeList){
            result.put(code,new FundInfo(code));
            codeParam.append("fu_").append(code).append(",");
        }
        String s = instance.sendGet("https://hq.sinajs.cn/etag.php?_="+System.currentTimeMillis()+"&list="+codeParam.toString());
        for(String str:s.split(NEW_LINE)){
            Matcher matcher = FUND_INFO_PATTERN.matcher(str);
            if(matcher.find()){
                FundInfo fund = result.get(matcher.group(1));
                if( fund != null){
                    String infos = matcher.group(2);
                    if(StringHelper.isEmpty(infos)){
                        log.error("找不到:[{}]的相关信息",matcher.group(1));
                        break;
                    }
                    String[] split = infos.split(",");

                    fund.setName(split[0]);
                    fund.setUpdateDate(DateHelper.parseDate(split[7]+" "+split[1]));
                    fund.setPrice(new BigDecimal(split[2]));
                    fund.setYesterdayPrice(new BigDecimal(split[3]));
                    fund.setSumPrice(new BigDecimal(split[4]));
                }
            }
        }
        return result;
    }

    /**
     * 获取历史行情
     * @param code
     * @return
     */
    public List<HistFundInfo> getHistQuotes(String code){
        List<HistFundInfo> resultList = Lists.newArrayList();
        DBParam param = new DBParam();
        int page = 1;
        String callback = "jQuery1112005770961188407431_1573020243009";
        param.set("callback",callback);
        param.set("symbol",code);

        while(true){
            param.set("page",page);
            param.set("_",System.currentTimeMillis());
            String s = instance.sendGet("http://stock.finance.sina.com.cn/fundInfo/api/openapi.php/CaihuiFundInfoService.getNav",param);
            if(StringHelper.isEmpty(s)){
                return resultList;
            }
            s = s.substring(s.indexOf("(") + 1, s.length() - 2);
            JSONObject jsonObject = JSONObject.parseObject(s);
            jsonObject = jsonObject.getJSONObject("result").getJSONObject("data");
            JSONArray data = jsonObject.getJSONArray("data");
            if(data.isEmpty()){
                break;
            }
            data.forEach(model->{
                JSONObject current = (JSONObject) model;
                Date date = current.getDate("fbrq");
                int backupDate = Integer.parseInt(DateHelper.formatDate(date, "yyyyMMdd"));
                HistFundInfo fundInfo = new HistFundInfo(backupDate,current.getBigDecimal("jjjz"),current.getBigDecimal("ljjz"));
                fundInfo.setCode(code);
                resultList.add(fundInfo);
            });
            page++;
        }
        return resultList;
    }
}
