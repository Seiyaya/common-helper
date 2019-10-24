package xyz.seiyaya.common.helper;

import org.junit.Test;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/24 18:26
 */
public class HttpTester {

    @Test
    public void testPOST(){
        DBParam basic = new DBParam();
        basic.set("apiVersion","4");

        DBParam header = new DBParam();

        HttpHelper.getHttpUtils().sendPost("https://14.17.52.198:10001/play/getmatchlist",basic,header);
    }
}
