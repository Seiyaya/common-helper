package xyz.seiyaya.common.quartz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.common.base.BaseMapper;
import xyz.seiyaya.common.base.BaseService;
import xyz.seiyaya.common.base.impl.BaseServiceImpl;
import xyz.seiyaya.common.quartz.bean.QuartzLog;
import xyz.seiyaya.common.quartz.bean.vo.QuartzLogVo;
import xyz.seiyaya.common.quartz.mapper.QuartzLogMapper;
import xyz.seiyaya.common.quartz.service.QuartzLogService;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:21
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
public class QuartzLogServiceImpl extends BaseServiceImpl<QuartzLog,Long> implements QuartzLogService {

    @Resource
    private QuartzLogMapper quartzLogMapper;

    @Override
    public BaseMapper<QuartzLog, Long> getMapper() {
        return quartzLogMapper;
    }
}
