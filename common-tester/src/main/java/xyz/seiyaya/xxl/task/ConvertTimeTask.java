package xyz.seiyaya.xxl.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import org.springframework.stereotype.Component;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/30 15:05
 */
@Component
public class ConvertTimeTask extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        return ReturnT.SUCCESS;
    }
}
