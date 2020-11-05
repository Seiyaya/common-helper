package xyz.seiyaya.common.web.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.common.bean.Address;
import xyz.seiyaya.common.bean.User;
import xyz.seiyaya.common.web.boot.Application;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/11/3 17:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DictJsonSerializerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testJsonSerializer() throws JsonProcessingException {
        User user = new User();
        user.setStatus(1);
        user.setAddressList(Lists.newArrayList(new Address(1L,"深圳市",13,1),
                new Address(1L,"河源市",13,0)));
        String s = objectMapper.writeValueAsString(user);
        System.out.println(s);
    }
}
