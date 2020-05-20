package xyz.seiyaya.app;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
