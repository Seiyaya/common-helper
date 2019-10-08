package xyz.seiyaya.mybatis.generate.quartz.service.impl;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import xyz.seiyaya.common.base.mapper.BaseMapper;
import xyz.seiyaya.common.base.service.impl.BaseServiceImpl;
import xyz.seiyaya.mybatis.generate.quartz.mapper.UploadPicMapper;
import xyz.seiyaya.mybatis.generate.quartz.UploadPic;
import xyz.seiyaya.mybatis.generate.quartz.service.UploadPicService;

import java.util.List;
import java.util.Map;

/**
 * 图片上传ServiceImpl
 *
 * @author seiyaya
 * @version 1.0.0
 * @date 2019-10-08
 */
@Service
public class UploadPicServiceImpl extends BaseServiceImpl<UploadPic, Long> implements UploadPicService {

    private static final Logger logger = LoggerFactory.getLogger(UploadPicServiceImpl.class);

    @Resource
    private UploadPicMapper uploadPicMapper;

	@Override
	public BaseMapper<UploadPic, Long> getMapper() {
		return uploadPicMapper;
	}

	@Override
	public int save(UploadPic uploadPic){
		return uploadPicMapper.save(uploadPic);
	}

	@Override
	public int update(UploadPic uploadPic){
		return uploadPicMapper.update(uploadPic);
	}

	@Override
	public int updateByMap(Map<String, Object> params){
		return uploadPicMapper.updateSelective(params);
	}

	@Override
	public List<UploadPic> select(Map<String, Object> params){
        return uploadPicMapper.listSelective(params);
    }

    @Override
    public Page<UploadPic> page(Integer current, Integer pageSize , Map<String, Object> params){
        PageHelper.startPage(current, pageSize);
        return (Page<UploadPic>)uploadPicMapper.listSelective(params);
    }

    @Override
    public UploadPic find(Map<String, Object> params){
    return uploadPicMapper.findSelective(params);
    }

    @Override
    public UploadPic findByPrimary(Long id){
    return uploadPicMapper.findByPrimary(id);
    }

}