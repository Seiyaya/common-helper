package xyz.seiyaya;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.common.cache.helper.HttpHelper;
import xyz.seiyaya.stock.StockApplication;
import xyz.seiyaya.stock.bean.WeiBo;
import xyz.seiyaya.stock.mapper.WeiBoMapper;

import javax.annotation.Resource;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/3/18 17:36
 */
@Slf4j
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = StockApplication.class)
public class WeiBoTester {

    @Resource
    private WeiBoMapper weiBoMapper;

    @Test
    public void getData(){

        String prefix = "https://m.weibo.cn/api/container/getIndex?uid=5876472419&t=0&luicode=10000011&lfid=100103type%3D3%26q%3D%E9%B8%9F%E5%8F%94%26t%3D0&containerid=1076035876472419&since_id=";
        String sinceId = "4481308200193884";
        HttpHelper httpUtils = HttpHelper.getHttpUtils();
        while(true){
            String url = prefix + sinceId;
            String result = httpUtils.sendGet(url);
            JSONObject topObject = JSONObject.parseObject(result);
            JSONObject cardListInfo = topObject.getJSONObject("data").getJSONObject("cardlistInfo");
            log.info("result:{}",topObject.getJSONObject("data").getJSONArray("cards"));
            JSONArray cardArray = topObject.getJSONObject("data").getJSONArray("cards");
            cardArray.forEach(model ->{
                JSONObject cardObject = (JSONObject) model;
                JSONObject mblog = cardObject.getJSONObject("mblog");
                WeiBo build = WeiBo.builder().createAt(mblog.getString("created_at")).id(mblog.getLong("id")).text(mblog.getString("text")).build();
                try {
                    weiBoMapper.insert(build);
                }catch (Exception e){
                    log.error("",e);
                }
            });
            if(!"1".equals(topObject.getString("ok"))){
                break;
            }
            sinceId = cardListInfo.getString("since_id");
        }
    }
}
