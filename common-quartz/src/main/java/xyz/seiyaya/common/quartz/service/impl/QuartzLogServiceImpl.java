package xyz.seiyaya.common.quartz.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import xyz.seiyaya.common.base.impl.BaseServiceImpl;
import xyz.seiyaya.common.quartz.bean.QuartzLog;
import xyz.seiyaya.common.quartz.service.QuartzLogService;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:21
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class QuartzLogServiceImpl extends BaseServiceImpl<QuartzLog,Mapper<QuartzLog>> implements QuartzLogService {
}
