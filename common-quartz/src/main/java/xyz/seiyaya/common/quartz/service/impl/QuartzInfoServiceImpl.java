package xyz.seiyaya.common.quartz.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.common.base.impl.BaseServiceImpl;
import xyz.seiyaya.common.quartz.bean.QuartzInfo;
import xyz.seiyaya.common.quartz.mapper.QuartzInfoMapper;
import xyz.seiyaya.common.quartz.service.QuartzInfoService;

import java.util.List;


/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:21
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class QuartzInfoServiceImpl extends BaseServiceImpl<QuartzInfo,QuartzInfoMapper> implements QuartzInfoService{

    @Override
    public List<QuartzInfo> getRealList(QuartzInfo quartzInfo) {
        return this.mapper.getRealList(quartzInfo);
    }
}
