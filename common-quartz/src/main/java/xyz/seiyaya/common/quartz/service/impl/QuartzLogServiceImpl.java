package xyz.seiyaya.common.quartz.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import xyz.seiyaya.common.base.impl.BaseServiceImpl;
import xyz.seiyaya.common.quartz.bean.QuartzLog;
import xyz.seiyaya.common.quartz.mapper.QuartzLogMapper;
import xyz.seiyaya.common.quartz.service.QuartzLogService;

import javax.annotation.Resource;

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
    public Mapper<QuartzLog> getMapper() {
        return quartzLogMapper;
    }
}
