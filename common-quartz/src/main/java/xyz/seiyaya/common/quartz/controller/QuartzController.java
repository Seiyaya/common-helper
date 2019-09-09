package xyz.seiyaya.common.quartz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.common.base.BaseController;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.quartz.bean.dto.QuartzInfoDto;
import xyz.seiyaya.common.quartz.bean.dto.QuartzLogDto;
import xyz.seiyaya.common.quartz.utils.QuartzHelper;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 14:59
 */
@RestController
@RequestMapping("/quartz")
@Slf4j
public class QuartzController extends BaseController {

    /**
     * 定时任务列表(分页)
     * @param quartzInfoDto
     * @return
     */
    @RequestMapping("/page")
    public ResultBean pageList(@RequestBody QuartzInfoDto quartzInfoDto){
        return new ResultBean();
    }

    /**
     * 添加定时任务
     * @param quartzInfoDto
     * @return
     */
    @RequestMapping("/add")
    public ResultBean add(@RequestBody QuartzInfoDto quartzInfoDto) throws Exception {
        QuartzHelper.addJob(quartzInfoDto);
        return new ResultBean();
    }

    /**
     * 更新定时任务
     * @param quartzInfoDto
     * @return
     */
    @RequestMapping("/update")
    public ResultBean update(@RequestBody QuartzInfoDto quartzInfoDto){
        return new ResultBean();
    }

    /**
     * 立即执行一个定时任务
     * @param quartzInfoDto
     * @return
     */
    @RequestMapping("/justRun")
    public ResultBean justRun(@RequestBody QuartzInfoDto quartzInfoDto){
        return new ResultBean();
    }

    /**
     * 全部的定时任务列表
     * @param quartzInfoDto
     * @return
     */
    @RequestMapping("/list")
    public ResultBean list(@RequestBody QuartzInfoDto quartzInfoDto){
        return new ResultBean();
    }

    /**
     * 日志分页
     * @param quartzLogDto
     * @return
     */
    @RequestMapping("/log/page")
    public ResultBean logPage(@RequestBody QuartzLogDto quartzLogDto){
        return new ResultBean();
    }
}
