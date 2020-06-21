package xyz.seiyaya.activiti.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.activiti.bean.dto.StartProcessDTO;
import xyz.seiyaya.boot.ProcessApplication;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.helper.SnowflakeIdHelper;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/6/18 16:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProcessApplication.class)
@EnableAutoConfiguration
@Slf4j
public class ProcessServiceTest {

    @Autowired
    private ProcessService processService;


    @Test
    public void testStartProcess(){
        String processType = "COMMIT_HOMEWORK";
        StartProcessDTO startProcess = StartProcessDTO.builder()
                .processType(processType)
                .teamId(1L)
                .userId(1L)
                .processTitle("张三的作业提交").serialNo(SnowflakeIdHelper.nextStringId()).build();
        ResultBean resultBean = processService.startProcess(startProcess);
        log.info("result:{} ", resultBean);
    }
}
