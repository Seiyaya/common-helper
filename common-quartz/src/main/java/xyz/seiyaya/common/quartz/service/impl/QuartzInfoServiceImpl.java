package xyz.seiyaya.common.quartz.service.impl;

import io.netty.util.internal.UnstableApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.common.base.BaseMapper;
import xyz.seiyaya.common.base.BaseService;
import xyz.seiyaya.common.base.impl.BaseServiceImpl;
import xyz.seiyaya.common.quartz.bean.QuartzInfo;
import xyz.seiyaya.common.quartz.bean.vo.QuartzInfoVo;
import xyz.seiyaya.common.quartz.mapper.QuartzInfoMapper;
import xyz.seiyaya.common.quartz.service.QuartzInfoService;

import javax.annotation.Resource;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:21
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
public class QuartzInfoServiceImpl extends BaseServiceImpl<QuartzInfo,Long> implements QuartzInfoService {

    @Resource
    private QuartzInfoMapper quartzInfoMapper;

    @Override
    public BaseMapper<QuartzInfo, Long> getMapper() {
        return quartzInfoMapper;
    }
}
