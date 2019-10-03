package xyz.seiyaya.common.quartz.controller;

import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ConstantBean;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.quartz.bean.QuartzInfo;
import xyz.seiyaya.common.quartz.bean.QuartzLog;
import xyz.seiyaya.common.quartz.bean.dto.QuartzInfoDto;
import xyz.seiyaya.common.quartz.bean.dto.QuartzLogDto;
import xyz.seiyaya.common.quartz.service.QuartzInfoService;
import xyz.seiyaya.common.quartz.service.QuartzLogService;
import xyz.seiyaya.common.quartz.utils.QuartzHelper;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 14:59
 */
@RestController
@RequestMapping("/quartz")
@Slf4j
public class QuartzController extends BaseController {


    @Autowired
    private QuartzInfoService quartzInfoService;

    @Autowired
    private QuartzLogService quartzLogService;

    /**
     * 定时任务列表(分页)
     * @param quartzInfoDto
     * @return
     */
    @RequestMapping("/page/{currentPage}/{pageSize}")
    public ResultBean pageList(@RequestBody QuartzInfoDto quartzInfoDto,
                               @PathVariable Integer currentPage,@PathVariable Integer pageSize){
        Page<QuartzInfo> page = quartzInfoService.page(quartzInfoDto,currentPage,pageSize);
        return new ResultBean().setData(page);
    }

    /**
     * 添加定时任务
     * @param quartzInfoDto
     * @return
     */
    @RequestMapping("/add")
    public ResultBean add(@RequestBody QuartzInfoDto quartzInfoDto) throws Exception {
        quartzInfoService.insert(quartzInfoDto);
        QuartzHelper.addJob(quartzInfoDto);
        return new ResultBean();
    }

    /**
     * 更新定时任务,启用和禁用
     * @param quartzInfoDto
     * @return
     */
    @RequestMapping("/update")
    public ResultBean update(@RequestBody QuartzInfoDto quartzInfoDto)throws Exception{
        QuartzInfo quartzInfo = quartzInfoService.getById(quartzInfoDto.getId());
        if (quartzInfo == null) {
            return new ResultBean("定时任务不存在",ResultBean.ResultConstant.CODE_ERROR.getCode());
        }
        if(quartzInfo.getState().equals(quartzInfo.getState())){
            return new ResultBean("参数错误",ResultBean.ResultConstant.CODE_ERROR.getCode());
        }

        if(ConstantBean.NUMBER_ZERO.equals(quartzInfo.getState())){
            //禁用定时任务
            QuartzHelper.removeJob(quartzInfo.getCode());
        }else if(ConstantBean.NUMBER_ONE.equals(quartzInfo.getState())){
            //启动定时任务
            Object clazz = Class.forName(quartzInfo.getClassName()).newInstance();
            QuartzHelper.addJob(quartzInfo);
        }
        quartzInfoService.updateById(quartzInfoDto);
        return new ResultBean();
    }

    /**
     * 立即执行一个定时任务
     * @param quartzInfoDto
     * @return
     */
    @RequestMapping("/justRun")
    public ResultBean justRun(@RequestBody QuartzInfoDto quartzInfoDto) throws Exception {
        QuartzInfo quartzInfo = quartzInfoService.getById(quartzInfoDto.getId());

        if (quartzInfo == null || ConstantBean.NUMBER_ZERO.equals(quartzInfo.getState())) {
            return new ResultBean("定时任务不存在或者已经停止",ResultBean.ResultConstant.CODE_ERROR.getCode());
        }

        QuartzHelper.startJob(quartzInfo.getCode());
        return new ResultBean();
    }

    /**
     * 全部的定时任务列表
     * @param quartzInfoDto
     * @return
     */
    @RequestMapping("/list")
    public ResultBean list(@RequestBody QuartzInfoDto quartzInfoDto){
        List<QuartzInfo> resultList = quartzInfoService.getList(quartzInfoDto);
        return new ResultBean().setData(resultList);
    }

    /**
     * 日志分页
     * @param quartzLogDto
     * @return
     */
    @RequestMapping("/log/page/{currentPage}/{pageSize}")
    public ResultBean logPage(@RequestBody QuartzLogDto quartzLogDto,
                              @PathVariable Integer currentPage,@PathVariable Integer pageSize){
        Page<QuartzLog> page = quartzLogService.page(quartzLogDto, currentPage, pageSize);
        return new ResultBean().setData(page);
    }
}
