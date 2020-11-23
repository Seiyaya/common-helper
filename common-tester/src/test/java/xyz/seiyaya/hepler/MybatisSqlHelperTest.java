package xyz.seiyaya.hepler;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.common.mybatis.helper.MybatisSqlHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/11/9 9:07
 */
@Slf4j
public class MybatisSqlHelperTest {

    public static void main(String[] args) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        ArrayList<Object> list = Lists.newArrayList();
        list.add(1);
        list.add(2);
        map.put("list",list);
        MybatisSqlHelper.SqlInfo sqlInfo = MybatisSqlHelper.getSqlInfo("xyz.seiyaya.mybatis.mapper.UserBeanMapper.findUser", map);
        log.info("[{}]",sqlInfo.formatSql());
    }
}
