package xyz.seiyaya.pvp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import xyz.seiyaya.common.config.Constant;
import xyz.seiyaya.common.helper.HttpHelper;
import xyz.seiyaya.pvp.bean.KingPic;
import xyz.seiyaya.pvp.bean.KingPicInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 用来下载王者荣耀壁纸
 * @author seiyaya
 * @date 2019/10/3 18:35
 */
@Slf4j
public class GloryKingsStarter {

    private static int totalPage = 1;

    private static String BASE_DIR = "E:/images/";

    public static final int LAST_END = 1584;

    public static void main(String[] args) {
        String itemUrl = "http://apps.game.qq.com/cgi-bin/ams/module/ishow/V1.0/query/workList_inc.cgi?activityId=2735&sVerifyCode=ABCD&sDataType=JSON&iListNum=20&totalpage=0&iOrder=0&iSortNumClose=1&jsoncallback=jQuery17106357796163591201_1570099686332&iAMSActivityId=51991&_everyRead=true&iTypeId=2&iFlowId=267733&iActId=2735&iModuleId=2735&_=1570099763797&page=";

        for(int i=0;i<totalPage;i++){
            boolean result = parsePic(itemUrl+i);
            if(result){
                return ;
            }
        }
    }

    /**
     * 解析图片
     * @param itemUrl
     */
    private static boolean parsePic(String itemUrl) {
        String result = HttpHelper.getHttpUtils().sendGet(itemUrl);
        if(result.startsWith("jQuery")){
            result = result.substring(result.indexOf("(") + 1, result.length() - 2);
            log.info("{}",result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            totalPage = jsonObject.getInteger("iTotalPages");
            JSONArray picArray = jsonObject.getJSONArray("List");
            for (Object model : picArray) {
                JSONObject picObject = (JSONObject) model;
                KingPicInfo kingPicInfo = new KingPicInfo();

                kingPicInfo.setInputTime((picObject.getString("dtInputDT")));
                kingPicInfo.setProductName(picObject.getString("sProdName"));
                int iProdId = picObject.getIntValue("iProdId");
                if(iProdId < LAST_END){
                    return false;
                }
                String dir = BASE_DIR + File.separator + kingPicInfo.getProductName();
                for(int i = 1; i<= Constant.IntegerConstant.INTEGER_6; i++){

                    String picUrl = picObject.getString("sProdImgNo_" + i);
                    KingPic kingPic = new KingPic(kingPicInfo.getId(),picUrl);
                    kingPic.setUrl(kingPic.getUrl().substring(0,kingPic.getUrl().lastIndexOf("/")+1));
                    File dirFile = new File(dir);
                    if(!dirFile.exists()){
                        dirFile.mkdirs();
                    }

                    File file = new File(dir+ File.separator + i + ".jpg");
                    if(!file.exists()){
                        try {
                            file.createNewFile();
                            byte[] bytes = HttpHelper.getHttpUtils().sendGetByte(kingPic.getUrl());
                            IOUtils.write(bytes,new FileOutputStream(file));
                        } catch (IOException e) {
                            log.info("创建文件失败,或者写入文件失败");
                        }
                    }

                    log.info("{}",kingPic);
                }
            }
        }
        return true;
    }

}
