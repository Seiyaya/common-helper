package xyz.seiyaya.common.annotation;

import com.google.common.collect.Lists;
import org.junit.Test;
import xyz.seiyaya.common.annotation.handle.CompareValueHelper;
import xyz.seiyaya.common.bean.Address;
import xyz.seiyaya.common.bean.User;
import xyz.seiyaya.common.helper.DateHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 修改日志测试
 * @author wangjia
 * @version 1.0
 * @date 2020/10/31 17:10
 */
public class UpdateLogInfoTest {

    @Test
    public void testUpdateLogInfo()throws Exception{
        User oldUser = new User();
        oldUser.setUserId("1");
        oldUser.setBirthday(new Date());

        oldUser.setAddressList(Lists.newArrayList(new Address(1L,"深圳市",13),new Address(2L,"广州市",14)));
        User newUser = new User();
        newUser.setUserId("2");
        newUser.setUsername("jack");
        newUser.setBirthday(DateHelper.getNDate(1));
        newUser.setAddressList(Lists.newArrayList(new Address(1L,"河源市",13),new Address(3L,"梅州市",17)));
        List<String> strings = CompareValueHelper.compareValue(oldUser, newUser);
        strings.forEach(System.out::println);
    }

    @Test
    public void timeZone(){
        String s = DateHelper.formatDate(Calendar.getInstance(Locale.CHINA).getTime(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(s);
    }
}
