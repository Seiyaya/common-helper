package xyz.seiyaya;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/21 9:54
 */
@Slf4j
public class CommonTester {

    public static void main(String[] args) {
        Map<String, Object> maps = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        maps.put("list", list);
        Object value = OgnlCache.getValue("list == null  or list.size() == 0", maps);
        System.out.println(value);
    }

    @Test
    public void StringSplit() {
        String item = "代理费回款-易仲勇-2019-11-14-8000.00元;代理费回款-东莞市创新智慧港物业管理有限公司-2019-11-14-5600.00元;";
        String[] str = item.split(";");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            if (str.length - 1 == i) {
                sb.append(str[i]);
            } else {
                sb.append(str[i]);
                sb.append("\n");
            }
        }
        System.out.println(sb.toString());
    }


    @Test
    public void testSwitch() throws Exception {
        Properties prop = new Properties();
        prop.load(new BufferedInputStream(new FileInputStream(new File("test.properties"))));

        System.out.println(prop.stringPropertyNames());
        int type = 3;
        switch (type) {
            case 1:
            case 2:
                System.out.println("22222");
            case 3:
            case 4:
            case 5:
                System.out.println("65666");
                break;
        }
    }

    @Test
    public void calcDifferenceList() {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list1.add(i + "");
            if (i % 2 == 0) {
                list2.add(i + "");
            }
        }

        list1.add("11");


        log.info("list1:{}",list1);
        log.info("list2:{}",list2);
        list1.removeAll(list2);
        log.info("list diff:{}",list1);
    }

    @Test
    public void retainAll(){
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list1.add(i + "");
            if (i % 2 == 0) {
                list2.add(i + "");
            }
        }

        list1.add("11");


        log.info("list1:{}",list1);
        log.info("list2:{}",list2);
        list1.retainAll(list2);
        log.info("list retain:{}",list1);
    }

    @Test
    public void testGetNowPrice() throws InterruptedException {
        StockTester test = new StockTester();
        test.methodB();
    }
}
