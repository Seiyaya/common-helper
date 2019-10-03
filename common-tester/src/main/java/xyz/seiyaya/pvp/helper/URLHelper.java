package xyz.seiyaya.pvp.helper;

import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;

/**
 * @author seiyaya
 * @date 2019/10/3 19:13
 */
@Slf4j
public class URLHelper {

    public static String urlDecoder(String str){
        if(str == null){
            return null;
        }
        try {
            return URLDecoder.decode(str, "utf-8");
        }catch (Exception e){
            log.info("decode 出现异常");
        }
        return null;
    }
}
