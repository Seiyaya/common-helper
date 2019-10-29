package xyz.seiyaya.common.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/24 18:26
 */
@Slf4j
public class HttpTester {

    @Test
    public void testPOST(){
        DBParam basic = new DBParam();
        String basicStr = "apiVersion:4|" +
                "option:0|" +
                "roleId:954364097|" +
                "cChannelId:3|" +
                "cClientVersionCode:2019346104|" +
                "cClientVersionName:3.46.104|" +
                "cCurrentGameId:20001|" +
                "cDeviceBrand:HUAWEI|" +
                "cDeviceCPU:armeabi-v7a$armeabi|" +
                "cDeviceId:43eae877f5f502084d469b5a5be9f9e34dd17825|" +
                "cDeviceImei:867029044553460|" +
                "cDeviceMac:02:00:00:00:00:00|" +
                "cDeviceMem:235438|" +
                "cDeviceModel:ELE-AL00|" +
                "cDeviceNet:WIFI|" +
                "cDevicePPI:480|" +
                "cDeviceSP:中国电信|" +
                "cDeviceScreenHeight:2139|" +
                "cDeviceScreenWidth:1080|" +
                "cGameId:20001|" +
                "cGzip:1|" +
                "cRand:1570592541901|" +
                "cSystem:android|" +
                "cSystemVersionCode:28|" +
                "cSystemVersionName:9|" +
                "deviceid:43eae877f5f502084d469b5a5be9f9e34dd17825|" +
                "gameAreaId:1|" +
                "gameId:20001|" +
                "gameOpenId:89809E1CC16CE8B6857600AF58A40E35|" +
                "gameRoleId:954364097|" +
                "gameServerId:1174|" +
                "gameUserSex:1|" +
                "token:cxv7rmGE|" +
                "userId:228478631";
        String[] basicSplit = basicStr.split("\\|");
        for(String s:basicSplit){
            String[] basicArray = s.split(":");
            basic.put(basicArray[0],basicArray[1]);
        }

        DBParam header = new DBParam();
        String headerStr = "x-tx-host:ssl.kohsocialapp.qq.com|" +
                "X-Online-Host:ssl.kohsocialapp.qq.com|" +
                "Host:ssl.kohsocialapp.qq.com|" +
                "X-Client-Proto:https|" +
                "NOENCRYPT:1|"+
                "User-Agent:okhttp/3.11.0|"+
                "Content-Type:application/x-www-form-urlencoded";

        String[] split = headerStr.split("\\|");
        for(String s : split){
            String[] headerArray = s.split(":");
            header.put(headerArray[0],headerArray[1]);
        }


        int win = 0;
        int total = 0;
        int cycleCount = 0;

        while(cycleCount < 10){
            String result = HttpHelper.getHttpUtils().sendPost("https://14.17.52.198:10001/play/getmatchlist", basic, header);
            JSONObject jsonObject = JSONObject.parseObject(result);
            int returnCode = jsonObject.getIntValue("returnCode");
            if(0 == returnCode){
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray recordList = data.getJSONArray("list");
                for(Object model:recordList){
                    JSONObject record = (JSONObject) model;
                    int gameSeq = record.getIntValue("gameSeq");
                    int gameresult = record.getIntValue("gameresult");
                    if(gameresult == 1){
                        win++;
                    }
                    total++;
                    log.info("{}--->{}",gameSeq,gameresult);
                }
                if(!data.getBooleanValue("hasMore")){
                    break;
                }else{
                    basic.set("lastTime",data.getIntValue("lastTime"));
                }
            }else if(-30003 == returnCode){
                log.error("登录态失效====");
                break;
            }else{
                log.error("其他未知问题:{}",jsonObject);
                break;
            }
            cycleCount++;
        }

        log.info("总胜场:{},总场次:{}",win,total);
        /**
         * "returnCode":-30003   登录态失效，请重新登录
         */
    }
}
