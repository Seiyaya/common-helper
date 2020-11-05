package xyz.seiyaya.common.msg;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.boot.ProcessApplication;
import xyz.seiyaya.common.cache.helper.SnowflakeIdHelper;
import xyz.seiyaya.common.msg.bean.ProcessMsg;
import xyz.seiyaya.common.msg.config.MsgConstant;
import xyz.seiyaya.common.msg.service.MsgService;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProcessApplication.class)
@EnableAutoConfiguration
@Slf4j
public class MsgTest {

    @Autowired
    private MsgService msgService;

    @Test
    public void testSendMsg() throws InterruptedException {
        String channel = MsgConstant.PROCESS;
        ProcessMsg processMsg = new ProcessMsg();
        processMsg.setProcessType("COMMIT_HOMEWORK");
        processMsg.setSerialNo(SnowflakeIdHelper.nextStringId());
        processMsg.setUserId(1L);
        msgService.sendMsg(channel,processMsg);

        TimeUnit.SECONDS.sleep(60);
    }
}
