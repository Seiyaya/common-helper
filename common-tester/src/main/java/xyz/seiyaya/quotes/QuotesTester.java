package xyz.seiyaya.quotes;

import org.junit.Test;
import xyz.seiyaya.common.helper.DBParam;
import xyz.seiyaya.common.helper.DateHelper;
import xyz.seiyaya.common.helper.HttpHelper;
import xyz.seiyaya.quotes.bean.Fund;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/6 10:03
 */
public class QuotesTester {

    private int count = 0;

    String[] array = {"002207","320007","000478","001480","161725","161724","004753","006098","000961"};

    @Test
    public void testGetFundPrice(){
        //https://hq.sinajs.cn/etag.php?_=1573005850196&list=fu_003096,fu_004753
        StringBuilder result = new StringBuilder();
        DBParam param = new DBParam();
        for(int i=0;i<array.length;i++){
            param.put(array[i],new Fund(array[i]));
            result.append("fu_").append(array[i]).append(",");
        }

        result.deleteCharAt(result.length()-1);
        System.out.println(result);


        String s = HttpHelper.getHttpUtils().sendGet("https://hq.sinajs.cn/etag.php?_"+System.currentTimeMillis()+"&list="+result.toString());
        System.out.println(s);

        Pattern r = Pattern.compile("_(\\d*)=\"(.*?)\"");
        for(String str:s.split("\n")){
            Matcher matcher = r.matcher(str);
            if(matcher.find()){
                Fund fund = param.getT(matcher.group(1),Fund.class);
                if( fund != null){
                    String infos = matcher.group(2);
                    String[] split = infos.split(",");

                    fund.setName(split[0]);
                    fund.setUpdateDate(DateHelper.parseDate(split[7]+" "+split[1]));
                    fund.setNowPrice(new BigDecimal(split[2]));
                    fund.setYesterdayPrice(new BigDecimal(split[3]));
                }
            }
        }

        System.out.println(param);
    }

    @Test
    public void testStack(){
        fun();
    }

    public void fun(){
        count++;
        if(count < 100000){
            fun();
        }
    }
}
