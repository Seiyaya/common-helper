package xyz.seiyaya.common.service.impl;

import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import xyz.seiyaya.common.base.BaseMapper;
import xyz.seiyaya.common.base.impl.BaseServiceImpl;
import xyz.seiyaya.common.bean.RequestLog;
import xyz.seiyaya.common.mapper.RequestLogMapper;
import xyz.seiyaya.common.service.RequestLogService;

import javax.annotation.Resource;

/**
 * 请求日志ServiceImpl
 *
 * @author seiyaya
 * @version 1.0.0
 * @date 2019-10-25
 */
@Service
public class RequestLogServiceImpl extends BaseServiceImpl<RequestLog, Integer> implements RequestLogService {

    @Resource
    private RequestLogMapper requestLogMapper;

    @Override
    public Mapper<RequestLog> getMapper() {
        return requestLogMapper;
    }
}