package xyz.seiyaya.common.quartz.service;

import xyz.seiyaya.common.base.BaseService;
import xyz.seiyaya.common.quartz.bean.QuartzInfo;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:21
 */
public interface QuartzInfoService extends BaseService<QuartzInfo,Long> {

    List<QuartzInfo> getRealList(QuartzInfo quartzInfo);
}
