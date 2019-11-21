package xyz.seiyaya;

import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.OgnlCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/21 9:54
 */
public class CommonTester {

    public static void main(String[] args) {
        Map<String,Object> maps = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        maps.put("list",list);
        Object value = OgnlCache.getValue("list == null  or list.size() == 0", maps);
        System.out.println(value);
    }
}
