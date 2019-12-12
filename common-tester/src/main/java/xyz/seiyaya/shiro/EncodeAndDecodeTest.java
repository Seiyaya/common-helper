package xyz.seiyaya.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

/**
 * shiro的编码和解码测试
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:21
 */
@Slf4j
public class EncodeAndDecodeTest {

    @Test
    public void testEncodeAndDecode(){
        String str = "hello";
        String encodeToString = Base64.encodeToString(str.getBytes());
        log.info("base64 编码后的内容:{}",encodeToString);

        String str2 = Base64.decodeToString(encodeToString);
        log.info("base64 解码后的内容:{}",str2);
    }

    @Test
    public void testMD5(){
        String str = "hello";
        String salt = "123";
        String md5 = new Md5Hash(str, salt,1).toString();
        String md5Two = new Md5Hash(str, salt,2).toString();
        log.info("md5转换后的内容:{} --> {}",md5,md5Two);
        /**
         * 散列一次: 86fcb4c0551ea48ede7df5ed9626eee7
         * 散列两次: c942f011ced5f36de066dd2d948538cb
         */

        log.info("散列一次的结果再次散列:{}", new Md5Hash(new Md5Hash("hello",salt).toString(),salt).toString());
    }
}
