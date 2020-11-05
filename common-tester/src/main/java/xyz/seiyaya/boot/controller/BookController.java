package xyz.seiyaya.boot.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.seiyaya.boot.bean.Feedback;
import xyz.seiyaya.boot.dao.FeedBackMapper;
import xyz.seiyaya.boot.helper.EmojiFilterUtils;
import xyz.seiyaya.common.bean.ResultBean;
import xyz.seiyaya.common.cache.helper.StringHelper;

import javax.annotation.Resource;

/**
 * @author seiyaya
 * @date 2019/11/3 23:48
 */
@RestController
public class BookController {

    @Resource
    private FeedBackMapper feedBackMapper;

    @RequestMapping("/book/addContent")
    public ResultBean addContent(@RequestBody Feedback feedback){
        feedback.setId((long) (Math.random()));
        feedback.setUserId((long) (Math.random()));
        feedBackMapper.insertSelective(feedback);
        return new ResultBean();
    }

    @RequestMapping("/book/updateContent")
    public ResultBean updateContent(@RequestBody Feedback feedback){
        String result = EmojiFilterUtils.filterEmoji(feedback.getContent());
        result = result.replace(" ", "");
        if(StringHelper.isEmpty(result)){
            return new ResultBean().setError("暂不支持emoji表情或者输入为空");
        }
        feedback.setId((long) (Math.random()));
        feedback.setUserId((long) (Math.random()));
        feedBackMapper.insertSelective(feedback);
        return new ResultBean();
    }
}
