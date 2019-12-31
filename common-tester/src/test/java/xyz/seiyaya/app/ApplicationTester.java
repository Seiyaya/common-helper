package xyz.seiyaya.app;

import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.xxl.task.ConvertTimeTask;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/30 15:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest(value = "xyz.seiyaya", classes = xyz.seiyaya.TesterApplication.class)
@EnableAutoConfiguration
@Slf4j
public class ApplicationTester {

    @Autowired
    private ConvertTimeTask convertTimeTask;

    @Test
    public void testConvertTimeTask() throws Exception {
        ReturnT<String> execute = convertTimeTask.execute("2019-12");
        log.info("{}",execute);
    }
}
