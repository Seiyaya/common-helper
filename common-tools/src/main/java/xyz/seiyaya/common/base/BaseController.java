package xyz.seiyaya.common.base;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import xyz.seiyaya.common.bean.ResultBean;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:00
 */
@Slf4j
public class BaseController {

    @ExceptionHandler({Exception.class})
    public void exceptionHandler(Exception e, HttpServletResponse response) throws IOException {
        log.error("发生了全局异常:", e);
        ResultBean resultBean = new ResultBean(ResultBean.ResultConstant.CODE_ERROR.getMsg(),ResultBean.ResultConstant.CODE_ERROR.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(resultBean));
    }
}
